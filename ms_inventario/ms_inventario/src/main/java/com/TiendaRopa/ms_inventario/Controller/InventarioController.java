package com.TiendaRopa.ms_inventario.Controller;

import com.TiendaRopa.ms_inventario.DTO.InventarioDTO;
import com.TiendaRopa.ms_inventario.DTO.MovimientosInventarioDTO;
import com.TiendaRopa.ms_inventario.Model.InventarioModel;
import com.TiendaRopa.ms_inventario.Model.MovimientosInventarioModel;
import com.TiendaRopa.ms_inventario.Services.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Gestión de stock y movimientos de inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    @Operation(summary = "Listar todo el inventario",
        description = "Devuelve el inventario completo con el detalle de cada producto")
    @GetMapping
    public ResponseEntity<List<InventarioModel>> listarTodos() {
        return ResponseEntity.ok(inventarioService.obtenerInventariosConDetalles());
    }

    @Operation(summary = "Consultar stock de un producto",
        description = "Devuelve el registro de inventario asociado a un producto específico")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioModel> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerPorProductoId(productoId));
    }

    @Operation(summary = "Crear registro de inventario",
        description = "Inicializa el inventario para un producto nuevo con su stock inicial")
    @PostMapping
    public ResponseEntity<InventarioModel> crear(@Valid @RequestBody InventarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.crearInventario(dto));
    }

    @Operation(summary = "Registrar movimiento de inventario",
        description = "Registra una ENTRADA o SALIDA de stock para un producto. " +
            "Si el movimiento es SALIDA, valida que haya stock suficiente antes de aplicarlo " +
            "y lanza un error si la cantidad solicitada supera el stock disponible.")
    @PostMapping("/producto/{productoId}/movimiento")
    public ResponseEntity<InventarioModel> registrarMovimiento(
            @PathVariable Long productoId,
            @Valid @RequestBody MovimientosInventarioDTO dto) {
        return ResponseEntity.ok(inventarioService.registrarMovimiento(productoId, dto));
    }

    @Operation(summary = "Historial de movimientos por producto",
        description = "Devuelve todas las entradas y salidas registradas para un producto")
    @GetMapping("/producto/{productoId}/movimientos")
    public ResponseEntity<List<MovimientosInventarioModel>> obtenerMovimientos(
            @PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerMovimientosPorProductoId(productoId));
    }
}