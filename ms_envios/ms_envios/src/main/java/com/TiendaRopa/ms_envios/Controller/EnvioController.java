package com.TiendaRopa.ms_envios.Controller;

import com.TiendaRopa.ms_envios.dto.EnvioDTO;
import com.TiendaRopa.ms_envios.Model.EnvioModel;
import com.TiendaRopa.ms_envios.service.EnvioService;
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
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@Tag(name = "Envios", description = "Operaciones relacionadas con los envios")
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    @Operation(summary = "Obtener todos los envios",
               description = "Obtiene una lista de todos los envios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EnvioModel.class)))
    })
    public ResponseEntity<List<EnvioModel>> obtenerTodos() {
        return ResponseEntity.ok(envioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envio por ID",
               description = "Obtiene un envio por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EnvioModel.class))),
        @ApiResponse(responseCode = "404", description = "Envio no encontrado",
            content = @Content)
    })
    public ResponseEntity<EnvioModel> obtenerPorId(
        @Parameter(description = "ID del envio", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener envios por usuario",
               description = "Obtiene todos los envios de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EnvioModel.class)))
    })
    public ResponseEntity<List<EnvioModel>> obtenerPorUsuario(
        @Parameter(description = "ID del usuario", required = true)
        @PathVariable Long usuarioId) {
        return ResponseEntity.ok(envioService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener envio por pedido",
               description = "Obtiene el envio de un pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EnvioModel.class))),
        @ApiResponse(responseCode = "404", description = "Envio no encontrado",
            content = @Content)
    })
    public ResponseEntity<EnvioModel> obtenerPorPedido(
        @Parameter(description = "ID del pedido", required = true)
        @PathVariable Long pedidoId) {
        return ResponseEntity.ok(envioService.obtenerPorPedido(pedidoId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo envio",
               description = "Crea un nuevo envio en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Envio creado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EnvioModel.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content)
    })
    public ResponseEntity<EnvioModel> crear(@Valid @RequestBody EnvioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crear(dto));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del envio",
               description = "Actualiza el estado de un envio existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = EnvioModel.class))),
        @ApiResponse(responseCode = "404", description = "Envio no encontrado",
            content = @Content)
    })
    public ResponseEntity<EnvioModel> actualizarEstado(
        @Parameter(description = "ID del envio", required = true)
        @PathVariable Long id,
        @Parameter(description = "Nuevo estado del envio", required = true)
        @RequestParam String estado) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un envio",
               description = "Elimina un envio por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Envio eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Envio no encontrado",
            content = @Content)
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID del envio", required = true)
        @PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}