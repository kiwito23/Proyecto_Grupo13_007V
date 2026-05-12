package com.TiendaRopa.ms_pagos.controller;

import com.TiendaRopa.ms_pagos.dto.PagoDTO;
import com.TiendaRopa.ms_pagos.model.Pago;
import com.TiendaRopa.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pago> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagoService.obtenerPorPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crear(dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}