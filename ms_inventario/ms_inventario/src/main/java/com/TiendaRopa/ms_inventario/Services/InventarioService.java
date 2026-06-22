package com.TiendaRopa.ms_inventario.Services;

import com.TiendaRopa.ms_inventario.DTO.InventarioDTO;
import com.TiendaRopa.ms_inventario.DTO.MovimientosInventarioDTO;
import com.TiendaRopa.ms_inventario.Model.InventarioModel;
import com.TiendaRopa.ms_inventario.Model.MovimientosInventarioModel;
import com.TiendaRopa.ms_inventario.Repositories.InventarioRepository;
import com.TiendaRopa.ms_inventario.Repositories.MovimientosInventarioRepository;
import com.TiendaRopa.ms_inventario.Exceptions.InventarioNotFoundException;
import com.TiendaRopa.ms_inventario.Exceptions.InventarioDuplicadoException;
import com.TiendaRopa.ms_inventario.Exceptions.StockInsuficienteException;
import com.TiendaRopa.ms_inventario.Exceptions.TipoMovimientoInvalidoException;
import com.TiendaRopa.ms_inventario.Exceptions.ProductoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientosInventarioRepository movimientosInventarioRepository;
    private final WebClient webClientProductos;

    public List<InventarioModel> obtenerInventariosConDetalles() {
        log.info("Listando todos los registros del inventario");
        return inventarioRepository.findAll();
    }

    public InventarioModel obtenerPorProductoId(Long productoId) {
        log.info("Obteniendo inventario para el producto ID: {}", productoId);
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> {
                    log.error("Inventario no encontrado para el producto ID: {}", productoId);
                    return new InventarioNotFoundException("Inventario no encontrado para el producto ID: " + productoId);
                });
    }

    public InventarioModel crearInventario(InventarioDTO inventarioDTO) {
        log.info("Creando nuevo inventario para el producto ID: {}", inventarioDTO.getProductoId());

        validarProductoExistente(inventarioDTO.getProductoId());

        if (inventarioRepository.existsByProductoId(inventarioDTO.getProductoId())) {
            log.error("Ya existe un inventario para el producto ID: {}", inventarioDTO.getProductoId());
            throw new InventarioDuplicadoException("Ya existe un inventario para el producto ID: " + inventarioDTO.getProductoId());
        }

        InventarioModel inventario = new InventarioModel();
        inventario.setProductoId(inventarioDTO.getProductoId());
        inventario.setStockActual(inventarioDTO.getStockActual());
        inventario.setStockMinimo(inventarioDTO.getStockMinimo());
        inventario.setUltimaActualizacion(LocalDateTime.now());

        InventarioModel inventarioGuardado = inventarioRepository.save(inventario);
        log.info("Inventario creado exitosamente para el producto ID: {}", inventarioDTO.getProductoId());
        return inventarioGuardado;
       
    }

    public InventarioModel registrarMovimiento(Long productoId, MovimientosInventarioDTO movimientoDTO) {
        log.info("Registrando movimiento de inventario para el producto ID: {}", productoId);

        // Regla de negocio: la cantidad debe ser mayor a cero
        if (movimientoDTO.getCantidad() == null || movimientoDTO.getCantidad() <= 0) {
            log.error("Cantidad inválida en movimiento: {}", movimientoDTO.getCantidad());
            throw new TipoMovimientoInvalidoException("La cantidad del movimiento debe ser mayor a cero");
        }

        InventarioModel inventario = obtenerPorProductoId(productoId);

        MovimientosInventarioModel.TipoMovimiento tipoMovimiento;
        try {
            tipoMovimiento = MovimientosInventarioModel.TipoMovimiento.valueOf(movimientoDTO.getTipoMovimiento().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Tipo de movimiento inválido: {}", movimientoDTO.getTipoMovimiento());
            throw new TipoMovimientoInvalidoException("Tipo de movimiento inválido: " + movimientoDTO.getTipoMovimiento() + ". Debe ser ENTRADA o SALIDA");
        }

        if (tipoMovimiento == MovimientosInventarioModel.TipoMovimiento.ENTRADA) {
            inventario.setStockActual(inventario.getStockActual() + movimientoDTO.getCantidad());
        } else if (tipoMovimiento == MovimientosInventarioModel.TipoMovimiento.SALIDA) {
            if (inventario.getStockActual() < movimientoDTO.getCantidad()) {
                log.error("No hay suficiente stock para realizar la salida. Stock actual: {}, Cantidad solicitada: {}", inventario.getStockActual(), movimientoDTO.getCantidad());
                throw new StockInsuficienteException("No hay suficiente stock para realizar la salida. Stock actual: " + inventario.getStockActual() + ", Cantidad solicitada: " + movimientoDTO.getCantidad());
            }
            inventario.setStockActual(inventario.getStockActual() - movimientoDTO.getCantidad());
        }

        inventario.setUltimaActualizacion(LocalDateTime.now());

        MovimientosInventarioModel movimiento = new MovimientosInventarioModel();
        movimiento.setInventario(inventario);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(movimientoDTO.getCantidad());
        movimiento.setMotivo(movimientoDTO.getMotivo());
        movimiento.setFechaMovimiento(LocalDateTime.now());

        inventarioRepository.save(inventario);
        movimientosInventarioRepository.save(movimiento);

        log.info("Movimiento de inventario registrado exitosamente para el producto ID: {}", productoId);
        return inventario;

    }

    public List<MovimientosInventarioModel> obtenerMovimientosPorProductoId(Long productoId) {
        log.info("Obteniendo movimientos de inventario para el producto ID: {}", productoId);
        InventarioModel inventario = obtenerPorProductoId(productoId);
        return movimientosInventarioRepository.findByInventarioId(inventario.getId());
    }

    public void validarProductoExistente(Long productoId) {
        // Regla de negocio: el ID del producto debe ser válido antes de consultar el servicio externo
        if (productoId == null || productoId <= 0) {
            log.error("ID de producto inválido para validar existencia: {}", productoId);
            throw new ProductoNoEncontradoException("El ID del producto debe ser un valor válido mayor a cero");
        }

        try{
            log.info("Validando existencia del producto ID: {} en el microservicio de productos", productoId);
            webClientProductos.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException.NotFound e) {
            log.error("El producto ID: {} no existe en el microservicio de productos", productoId);
            throw new ProductoNoEncontradoException("El producto ID: " + productoId + " no existe en el microservicio de productos");
        } catch (Exception e){
            log.error("No fue posible validar el producto ID: {} en el microservicio de productos: {}", productoId, e.getMessage());
            throw new ProductoNoEncontradoException("El producto ID: " + productoId + " no existe o el microservicio de productos no está disponible");
        }
    }
}