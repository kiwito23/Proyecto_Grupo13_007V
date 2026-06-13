package com.TiendaRopa.ms_tallas.controller;

import com.TiendaRopa.ms_tallas.dto.TallaDTO;
import com.TiendaRopa.ms_tallas.model.Talla;
import com.TiendaRopa.ms_tallas.service.TallaService;
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
@RequestMapping("/api/tallas")
@RequiredArgsConstructor
@Tag(name = "Tallas", description = "Operaciones relacionadas con las tallas de ropa")
public class TallaController {

    private final TallaService tallaService;

    @GetMapping
    @Operation(summary = "Obtener todas las tallas",
               description = "Obtiene una lista de todas las tallas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Talla.class)))
    })
    public ResponseEntity<List<Talla>> obtenerTodas() {
        return ResponseEntity.ok(tallaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener talla por ID",
               description = "Obtiene una talla por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Talla.class))),
        @ApiResponse(responseCode = "404", description = "Talla no encontrada",
            content = @Content)
    })
    public ResponseEntity<Talla> obtenerPorId(
        @Parameter(description = "ID de la talla", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(tallaService.obtenerPorId(id));
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener tallas activas",
               description = "Obtiene una lista de tallas con estado ACTIVO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Talla.class)))
    })
    public ResponseEntity<List<Talla>> obtenerActivas() {
        return ResponseEntity.ok(tallaService.obtenerActivas());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva talla",
               description = "Crea una nueva talla en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Talla creada exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Talla.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<Talla> crear(@Valid @RequestBody TallaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una talla",
               description = "Actualiza una talla existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Talla actualizada exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Talla.class))),
        @ApiResponse(responseCode = "404", description = "Talla no encontrada",
            content = @Content)
    })
    public ResponseEntity<Talla> actualizar(
        @Parameter(description = "ID de la talla", required = true)
        @PathVariable Long id,
        @Valid @RequestBody TallaDTO dto) {
        return ResponseEntity.ok(tallaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una talla",
               description = "Elimina una talla por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Talla eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Talla no encontrada",
            content = @Content)
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la talla", required = true)
        @PathVariable Long id) {
        tallaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}