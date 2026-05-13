package com.TiendaRopa.ms_resenas.controller;

import com.TiendaRopa.ms_resenas.dto.ResenaDTO;
import com.TiendaRopa.ms_resenas.model.Resena;
import com.TiendaRopa.ms_resenas.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping
    public ResponseEntity<List<Resena>> obtenerTodas() {
        return ResponseEntity.ok(resenaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(resenaService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Resena>> obtenerPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(resenaService.obtenerPorProducto(productoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(resenaService.obtenerPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody ResenaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(@PathVariable Long id,
            @Valid @RequestBody ResenaDTO dto) {
        return ResponseEntity.ok(resenaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}