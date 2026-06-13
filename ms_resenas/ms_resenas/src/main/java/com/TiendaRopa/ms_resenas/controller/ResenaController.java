package com.TiendaRopa.ms_resenas.controller;

import com.TiendaRopa.ms_resenas.dto.ResenaDTO;
import com.TiendaRopa.ms_resenas.model.Resena;
import com.TiendaRopa.ms_resenas.service.ResenaService;
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
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "Operaciones relacionadas con las reseñas de productos")
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping
    @Operation(summary = "Obtener todas las reseñas",
               description = "Obtiene una lista de todas las reseñas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class)))
    })
    public ResponseEntity<List<Resena>> obtenerTodas() {
        return ResponseEntity.ok(resenaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID",
               description = "Obtiene una reseña por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
            content = @Content)
    })
    public ResponseEntity<Resena> obtenerPorId(
        @Parameter(description = "ID de la reseña", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(resenaService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener reseñas por producto",
               description = "Obtiene todas las reseñas de un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class)))
    })
    public ResponseEntity<List<Resena>> obtenerPorProducto(
        @Parameter(description = "ID del producto", required = true)
        @PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.obtenerPorProducto(productoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener reseñas por usuario",
               description = "Obtiene todas las reseñas de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class)))
    })
    public ResponseEntity<List<Resena>> obtenerPorUsuario(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long usuarioId) {
        return ResponseEntity.ok(resenaService.obtenerPorUsuario(usuarioId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reseña",
               description = "Crea una nueva reseña de producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<Resena> crear(@Valid @RequestBody ResenaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reseña",
               description = "Actualiza una reseña existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
            content = @Content)
    })
    public ResponseEntity<Resena> actualizar(
        @Parameter(description = "ID de la reseña", required = true)
        @PathVariable Long id,
        @Valid @RequestBody ResenaDTO dto) {
        return ResponseEntity.ok(resenaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reseña",
               description = "Elimina una reseña por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada",
            content = @Content)
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la reseña", required = true)
        @PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recomendadas")
    @Operation(summary = "Obtener reseñas recomendadas",
               description = "Obtiene las 5 reseñas más recomendadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resena.class)))
    })
    public ResponseEntity<List<Resena>> obtenerRecomendadas(
        @Parameter(description = "Filtrar por recomendado true/false", required = false)
        @RequestParam(defaultValue = "true") Boolean recomendado) {
        return ResponseEntity.ok(resenaService.obtenerRecomendadas(recomendado));
    }
}