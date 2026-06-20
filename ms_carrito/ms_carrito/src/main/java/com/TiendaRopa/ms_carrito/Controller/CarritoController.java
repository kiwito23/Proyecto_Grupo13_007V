package com.TiendaRopa.ms_carrito.Controller;

import com.TiendaRopa.ms_carrito.DTO.ItemCarritoDTO;
import com.TiendaRopa.ms_carrito.Model.CarritoModel;
import com.TiendaRopa.ms_carrito.Model.ItemCarritoModel;
import com.TiendaRopa.ms_carrito.Services.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @Operation(summary = "Obtener carrito por usuario", 
        description = "Devuelve el carrito activo del usuario o crea uno nuevo si no existe")      
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoModel> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerOCrearCarrito(usuarioId));
    }

    @Operation(summary = "Obtener items del carrito", 
        description = "Devuelve la lista de items en el carrito del usuario")      
    @GetMapping("/usuario/{usuarioId}/items")
    public ResponseEntity<List<ItemCarritoModel>> obtenerItems(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerItems(usuarioId));
    }

     @Operation(summary = "Obtener oferta para producto", 
        description = "Devuelve el carrito con la oferta aplicada para el producto especificado")
    @GetMapping("/usuario/{usuarioId}/oferta")
    public ResponseEntity<CarritoModel> ofertaCarrito(
            @PathVariable Long usuarioId,
            @RequestParam Long productoId) {
        return ResponseEntity.ok(carritoService.ofertaCarrito(usuarioId, productoId));
    }

     @Operation(summary = "Agregar item al carrito", 
        description = "Agrega un item al carrito del usuario. Si el producto ya existe en el carrito, actualiza la cantidad")
    @PostMapping("/usuario/{usuarioId}/items")
    public ResponseEntity<CarritoModel> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ItemCarritoDTO dto) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, dto));
    }

    @Operation(summary = "Eliminar item del carrito", 
        description = "Elimina un item específico del carrito del usuario")
    @DeleteMapping("/usuario/{usuarioId}/items/{productoId}")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        carritoService.eliminarItem(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vaciar carrito", 
        description = "Elimina todos los items del carrito del usuario")
    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

}
