package com.TiendaRopa.ms_pagos.controller;

import com.TiendaRopa.ms_pagos.dto.PagoDTO;
import com.TiendaRopa.ms_pagos.model.Pago;
import com.TiendaRopa.ms_pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos")
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    @Operation(summary = "Obtener todos los pagos",
               description = "Obtiene una lista de todos los pagos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Pago.class)))
    })
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID",
               description = "Obtiene un pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    public ResponseEntity<Pago> obtenerPorId(
        @Parameter(description = "ID del pago", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener pagos por usuario",
               description = "Obtiene todos los pagos de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Pago.class)))
    })
    public ResponseEntity<List<Pago>> obtenerPorUsuario(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener pago por pedido",
               description = "Obtiene el pago de un pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    public ResponseEntity<Pago> obtenerPorPedido(
        @Parameter(description = "ID del pedido", required = true)
        @PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagoService.obtenerPorPedido(pedidoId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pago",
               description = "Crea un nuevo pago en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crear(dto));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pago",
               description = "Actualiza el estado de un pago existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    public ResponseEntity<Pago> actualizarEstado(
        @Parameter(description = "ID del pago", required = true)
        @PathVariable Long id,
        @Parameter(description = "Nuevo estado del pago", required = true)
        @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago",
               description = "Elimina un pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado",
            content = @Content)
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID del pago", required = true)
        @PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}