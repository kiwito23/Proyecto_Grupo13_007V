package com.TiendaRopa.ms_carrito.Services;

import com.TiendaRopa.ms_carrito.DTO.ItemCarritoDTO;
import com.TiendaRopa.ms_carrito.Model.CarritoModel;
import com.TiendaRopa.ms_carrito.Model.ItemCarritoModel;
import com.TiendaRopa.ms_carrito.Repositories.CarritoRepository;
import com.TiendaRopa.ms_carrito.Repositories.ItemCarritoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final WebClient webClientUsuarios;
    private final WebClient webClientProductos;
    private final WebClient webClientTallas; 

        public CarritoModel obtenerOCrearCarrito(Long usuarioId) {
        log.info("Obteniendo carrito para usuario id: {}", usuarioId);
        return carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .orElseGet(() -> {
                    // Valida que el usuario exista antes de crear carrito
                    validarUsuarioExiste(usuarioId);
                    log.info("Creando nuevo carrito para usuario id: {}", usuarioId);
                    CarritoModel nuevo = new CarritoModel();
                    nuevo.setUsuarioId(usuarioId);
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    nuevo.setActivo(true);
                    return carritoRepository.save(nuevo);
                });
    }

    public CarritoModel agregarItem(Long usuarioId, ItemCarritoDTO itemCarritoDTO) {
        log.info("Agregando producto {} talla {} al carrito del usuario {}", 
                        itemCarritoDTO.getProductoId(), itemCarritoDTO.getTallaId(), usuarioId);

        validarProductoExiste(itemCarritoDTO.getProductoId());
        validarTallaExiste(itemCarritoDTO.getTallaId());          // ← nuevo

        CarritoModel carrito = obtenerOCrearCarrito(usuarioId);

        itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), itemCarritoDTO.getProductoId())
                .ifPresentOrElse(
                        item -> {
                            log.info("Producto ya existe en carrito, actualizando cantidad");
                            item.setCantidad(item.getCantidad() + itemCarritoDTO.getCantidad());
                            itemCarritoRepository.save(item);
                        },
                        () -> {
                            ItemCarritoModel nuevoItem = new ItemCarritoModel();
                            nuevoItem.setCarrito(carrito);
                            nuevoItem.setProductoId(itemCarritoDTO.getProductoId());
                            nuevoItem.setCantidad(itemCarritoDTO.getCantidad());
                            nuevoItem.setPrecioUnitario(itemCarritoDTO.getPrecioUnitario());
                            nuevoItem.setTallaId(itemCarritoDTO.getTallaId());
                            itemCarritoRepository.save(nuevoItem);
                            log.info("Nuevo item agregado al carrito");
                        }
                );
        return carritoRepository.findById(carrito.getId()).orElseThrow();
    }

    public List<ItemCarritoModel> obtenerItems(Long usuarioId) {
        log.info("Obteniendo items del carrito del usuario id: {}", usuarioId);
        CarritoModel carrito = obtenerOCrearCarrito(usuarioId);
        return itemCarritoRepository.findByCarritoId(carrito.getId());
    }

    public void eliminarItem(Long usuarioId, Long productoId) {
        log.info("Eliminando producto {} del carrito del usuario {}", productoId, usuarioId);
        CarritoModel carrito = obtenerOCrearCarrito(usuarioId);
        ItemCarritoModel item = itemCarritoRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .orElseThrow(() -> new RuntimeException("El producto no está en el carrito"));
        itemCarritoRepository.delete(item);
        log.info("Item eliminado del carrito");
    }

    public void vaciarCarrito(Long usuarioId) {
        log.info("Vaciando carrito del usuario id: {}", usuarioId);
        CarritoModel carrito = obtenerOCrearCarrito(usuarioId);
        List<ItemCarritoModel> items = itemCarritoRepository.findByCarritoId(carrito.getId());
        itemCarritoRepository.deleteAll(items);
        log.info("Carrito vaciado para usuario id: {}", usuarioId);
    }

    private void validarUsuarioExiste(Long usuarioId) {
        try {
            log.info("Validando existencia de usuario id: {}", usuarioId);
            webClientUsuarios.get()
                    .uri("/api/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Usuario id: {} validado correctamente", usuarioId);
        } catch (Exception e) {
            log.error("Usuario no encontrado con id: {}", usuarioId);
            throw new RuntimeException("El usuario con id " + usuarioId + " no existe");
        }
    }

    private void validarProductoExiste(Long productoId) {
        try {
            log.info("Validando existencia de producto id: {}", productoId);
            webClientProductos.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Producto id: {} validado correctamente", productoId);
        } catch (Exception e) {
            log.error("Producto no encontrado con id: {}", productoId);
            throw new RuntimeException("El producto con id " + productoId + " no existe");
        }
    }

    private void validarTallaExiste(Long tallaId) {
        try {
            log.info("Validando talla id: {} en ms-tallas", tallaId);
            webClientTallas.get()
                    .uri("/api/tallas/{id}", tallaId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Talla id: {} validada correctamente", tallaId);
        } catch (Exception e) {
            log.error("Talla no encontrada en ms-tallas con id: {}", tallaId);
            throw new RuntimeException("La talla con id " + tallaId + " no existe");
        }
    }

}
