package com.TiendaRopa.ms_pedidos.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.TiendaRopa.ms_pedidos.DTO.EstadoDTO;
import com.TiendaRopa.ms_pedidos.DTO.PedidoDTO;
import com.TiendaRopa.ms_pedidos.Model.DetallePedidoModel;
import com.TiendaRopa.ms_pedidos.Model.PedidoModel;
import com.TiendaRopa.ms_pedidos.Repositories.PedidoRepository;
import com.TiendaRopa.ms_pedidos.Exceptions.PedidoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final WebClient webClientUsuarios;
    private final WebClient webClientInventario;
    private final WebClient webClientCarrito;
    private final WebClient webClientEnvios;
    private final WebClient webClientPagos;

    public List<PedidoModel> listarTodos() {
        log.info("Listando todos los pedidos");
        return pedidoRepository.findAll();
    }

    public List<PedidoModel> listarPorUsuario(Long usuarioId) {
        log.info("Listando pedidos del usuario id: {}", usuarioId);
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public PedidoModel obtenerPorId(Long id) {
        log.info("Buscando pedido con id: {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pedido no encontrado con id: {}", id);
                    return new PedidoNotFoundException("Pedido no encontrado con id: " + id);
                });
    }

    public PedidoModel actualizarEstado(Long id, EstadoDTO dto) {
        log.info("Actualizando estado del pedido id: {} a {}", id, dto.getEstado());
        PedidoModel pedido = obtenerPorId(id);

        PedidoModel.EstadoPedido nuevoEstado;
        try {
            nuevoEstado = PedidoModel.EstadoPedido.valueOf(dto.getEstado().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Estado inválido: {}", dto.getEstado());
            throw new PedidoNotFoundException("Estado inválido: " + dto.getEstado());
        }

        // Regla de negocio: no se puede cambiar estado de un pedido ENTREGADO o CANCELADO
        if (pedido.getEstado() == PedidoModel.EstadoPedido.ENTREGADO ||
            pedido.getEstado() == PedidoModel.EstadoPedido.CANCELADO) {
            throw new PedidoNotFoundException("No se puede modificar un pedido " + pedido.getEstado());
        }

        pedido.setEstado(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    // Llama a ms-usuarios para validar que existe
    private void validarUsuarioExiste(Long usuarioId) {
        try {
            webClientUsuarios.get()
                    .uri("/api/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Usuario {} validado", usuarioId);
        } catch (Exception e) {
            log.error("Usuario no encontrado: {}", usuarioId);
            throw new PedidoNotFoundException("El usuario con id " + usuarioId + " no existe");
        }
    }

    // Llama a ms-carrito para obtener los items
    private List<JsonNode> obtenerItemsCarrito(Long usuarioId) {
        try {
            log.info("Obteniendo items del carrito del usuario {}", usuarioId);
            JsonNode[] items = webClientCarrito.get()
                    .uri("/api/carrito/usuario/{usuarioId}/items", usuarioId)
                    .retrieve()
                    .bodyToMono(JsonNode[].class)
                    .block();
            return items != null ? List.of(items) : List.of();
        } catch (Exception e) {
            log.error("Error al obtener carrito del usuario {}: {}", usuarioId, e.getMessage());
            throw new PedidoNotFoundException("Error al obtener el carrito del usuario");
        }
    }

    // Llama a ms-inventario para registrar la salida de stock
    private void registrarSalidaInventario(Long productoId, Integer cantidad) {
        try {
            log.info("Registrando salida de {} unidades del producto {}", cantidad, productoId);
            String body = String.format(
                "{\"tipoMovimiento\":\"SALIDA\",\"cantidad\":%d,\"motivo\":\"Pedido confirmado\"}",
                cantidad
            );
            webClientInventario.post()
                    .uri("/api/inventario/producto/{productoId}/movimiento", productoId)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al registrar salida de inventario para producto {}: {}", productoId, e.getMessage());
            throw new PedidoNotFoundException("Stock insuficiente o error en inventario para producto id: " + productoId);
        }
    }

    private void vaciarCarrito(Long usuarioId) {
        try {
            log.info("Vaciando carrito del usuario {}", usuarioId);
            webClientCarrito.delete()
                    .uri("/api/carrito/usuario/{usuarioId}/vaciar", usuarioId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.warn("No se pudo vaciar el carrito del usuario {}: {}", usuarioId, e.getMessage());
        }
    }

    public PedidoModel crearDesdCarrito(PedidoDTO pedidoDTO) {
    log.info("Creando pedido para usuario id: {}", pedidoDTO.getUsuarioId());

    validarUsuarioExiste(pedidoDTO.getUsuarioId());

    List<JsonNode> items = obtenerItemsCarrito(pedidoDTO.getUsuarioId());
    if (items.isEmpty()) {
        log.warn("El carrito del usuario {} está vacío", pedidoDTO.getUsuarioId());
        throw new PedidoNotFoundException("El carrito está vacío, no se puede crear el pedido");
    }

    List<DetallePedidoModel> detalles = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    PedidoModel pedido = new PedidoModel();
    pedido.setUsuarioId(pedidoDTO.getUsuarioId());
    pedido.setDireccionEntrega(pedidoDTO.getDireccionEntrega());
    pedido.setEstado(PedidoModel.EstadoPedido.PENDIENTE);
    pedido.setFechaPedido(LocalDateTime.now());
    pedido.setFechaActualizacion(LocalDateTime.now());

    for (JsonNode item : items) {
        Long productoId = item.get("productoId").asLong();
        Integer cantidad = item.get("cantidad").asInt();
        BigDecimal precioUnitario = new BigDecimal(item.get("precioUnitario").asText());
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        registrarSalidaInventario(productoId, cantidad);

        DetallePedidoModel detalle = new DetallePedidoModel();
        detalle.setPedido(pedido);
        detalle.setProductoId(productoId);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);
        detalles.add(detalle);

        total = total.add(subtotal);
    }

    pedido.setTotal(total);
    pedido.setDetalles(detalles);

    PedidoModel guardado = pedidoRepository.save(pedido);
    log.info("Pedido creado con id: {} por un total de: {}", guardado.getId(), total);

    procesarPago(guardado.getId(), pedidoDTO.getUsuarioId(), total, pedidoDTO.getMetodoPago());
    crearEnvio(guardado.getId(), pedidoDTO.getUsuarioId(), pedidoDTO.getDireccionEntrega());
    vaciarCarrito(pedidoDTO.getUsuarioId());

    return guardado;

    }

    private void procesarPago(Long pedidoId, Long usuarioId, BigDecimal monto, String metodoPago) {
        try {
            log.info("Procesando pago de {} para usuario {} via {}", monto, usuarioId, metodoPago);
            String body = String.format(
                "{\"pedidoId\":%d,\"usuarioId\":%d,\"monto\":%s,\"metodoPago\":\"%s\"}",
                pedidoId, usuarioId, monto.toPlainString(), metodoPago
            );
            webClientPagos.post()
                    .uri("/api/pagos")
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Pago procesado correctamente para usuario {}", usuarioId);
        } catch (Exception e) {
            log.error("Error al procesar pago para usuario {}: {}", usuarioId, e.getMessage());
            throw new PedidoNotFoundException("Error al procesar el pago: " + e.getMessage());
        }
    }


    private void crearEnvio(Long pedidoId, Long usuarioId, String direccion) {
    try {
        log.info("Creando envío para pedido id: {}", pedidoId);
        String body = String.format(
            "{\"pedidoId\":%d,\"usuarioId\":%d,\"direccion\":\"%s\",\"ciudad\":\"Santiago\"}",
            pedidoId, usuarioId, direccion
            );
            
            webClientEnvios.post()
                    .uri("/api/envios")
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Envío creado correctamente para pedido {}", pedidoId);
        } catch (Exception e) {
            log.warn("No se pudo crear el envío para pedido {}: {}", pedidoId, e.getMessage());
            throw new PedidoNotFoundException("Error al crear el envío: " + e.getMessage());
        }

    }
}