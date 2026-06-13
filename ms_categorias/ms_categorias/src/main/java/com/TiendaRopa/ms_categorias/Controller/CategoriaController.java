package com.TiendaRopa.ms_categorias.Controller;

import com.TiendaRopa.ms_categorias.dto.CategoriaDTO;
import com.TiendaRopa.ms_categorias.model.Categoria;
import com.TiendaRopa.ms_categorias.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Operaciones relacionadas con las categorias de ropa")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorias",
               description = "Obtiene una lista de todas las categorias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Categoria.class)))
    })
    public ResponseEntity<List<Categoria>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoria por ID",
               description = "Obtiene una categoria por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
            content = @Content)
    })
    public ResponseEntity<Categoria> obtenerPorId(
        @Parameter(description = "ID de la categoria", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener categorias activas",
               description = "Obtiene una lista de categorias con estado ACTIVO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Categoria.class)))
    })
    public ResponseEntity<List<Categoria>> obtenerActivas() {
        return ResponseEntity.ok(categoriaService.obtenerActivas());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoria",
               description = "Crea una nueva categoria en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoria",
               description = "Actualiza una categoria existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria actualizada exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
            content = @Content)
    })
    public ResponseEntity<Categoria> actualizar(
        @Parameter(description = "ID de la categoria", required = true)
        @PathVariable Long id,
        @Valid @RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoria",
               description = "Elimina una categoria por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
            content = @Content)
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la categoria", required = true)
        @PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}