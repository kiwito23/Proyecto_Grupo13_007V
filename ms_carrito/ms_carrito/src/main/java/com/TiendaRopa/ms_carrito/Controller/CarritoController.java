package com.TiendaRopa.ms_carrito.Controller;

import com.TiendaRopa.ms_carrito.DTO.ItemCarritoDTO;
import com.TiendaRopa.ms_carrito.Model.CarritoModel;
import com.TiendaRopa.ms_carrito.Model.ItemCarritoModel;
import com.TiendaRopa.ms_carrito.Services.CarritoService;
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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoModel> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerOCrearCarrito(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/items")
    public ResponseEntity<List<ItemCarritoModel>> obtenerItems(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerItems(usuarioId));
    }

    @PostMapping("/usuario/{usuarioId}/items")
    public ResponseEntity<CarritoModel> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ItemCarritoDTO dto) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, dto));
    }

    @DeleteMapping("/usuario/{usuarioId}/items/{productoId}")
    public ResponseEntity<Void> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        carritoService.eliminarItem(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

}
