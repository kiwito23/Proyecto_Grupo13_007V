package com.TiendaRopa.ms_tallas.controller;

import com.TiendaRopa.ms_tallas.dto.TallaDTO;
import com.TiendaRopa.ms_tallas.model.Talla;
import com.TiendaRopa.ms_tallas.service.TallaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tallas")
@RequiredArgsConstructor
public class TallaController {

    private final TallaService tallaService;

    @GetMapping
    public ResponseEntity<List<Talla>> obtenerTodas() {
        return ResponseEntity.ok(tallaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talla> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tallaService.obtenerPorId(id));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Talla>> obtenerActivas() {
        return ResponseEntity.ok(tallaService.obtenerActivas());
    }

    @PostMapping
    public ResponseEntity<Talla> crear(@Valid @RequestBody TallaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Talla> actualizar(@PathVariable Long id,
            @Valid @RequestBody TallaDTO dto) {
        return ResponseEntity.ok(tallaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tallaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}