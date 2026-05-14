package com.TiendaRopa.ms_envios.controller;

import com.TiendaRopa.ms_envios.dto.EnvioDTO;
import com.TiendaRopa.ms_envios.Model.EnvioModel;
import com.TiendaRopa.ms_envios.service.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<EnvioModel>> obtenerTodos() {
        return ResponseEntity.ok(envioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioModel> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnvioModel>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(envioService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<EnvioModel> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(envioService.obtenerPorPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<EnvioModel> crear(@Valid @RequestBody EnvioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crear(dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<EnvioModel> actualizarEstado(@PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}