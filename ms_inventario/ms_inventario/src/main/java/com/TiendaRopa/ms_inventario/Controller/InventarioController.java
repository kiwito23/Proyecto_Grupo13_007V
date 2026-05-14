package com.TiendaRopa.ms_inventario.Controller;

import com.TiendaRopa.ms_inventario.DTO.InventarioDTO;
import com.TiendaRopa.ms_inventario.DTO.MovimientosInventarioDTO;
import com.TiendaRopa.ms_inventario.Model.InventarioModel;
import com.TiendaRopa.ms_inventario.Model.MovimientosInventarioModel;
import com.TiendaRopa.ms_inventario.Services.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioModel>> listarTodos() {
        return ResponseEntity.ok(inventarioService.obtenerInventariosConDetalles());
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioModel> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerPorProductoId(productoId));
    }

    @PostMapping
    public ResponseEntity<InventarioModel> crear(@Valid @RequestBody InventarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.crearInventario(dto));
    }

    @PostMapping("/producto/{productoId}/movimiento")
    public ResponseEntity<InventarioModel> registrarMovimiento(
            @PathVariable Long productoId,
            @Valid @RequestBody MovimientosInventarioDTO dto) {
        return ResponseEntity.ok(inventarioService.registrarMovimiento(productoId, dto));
    }

    @GetMapping("/producto/{productoId}/movimientos")
    public ResponseEntity<List<MovimientosInventarioModel>> obtenerMovimientos(
            @PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerMovimientosPorProductoId(productoId));
    }
}
