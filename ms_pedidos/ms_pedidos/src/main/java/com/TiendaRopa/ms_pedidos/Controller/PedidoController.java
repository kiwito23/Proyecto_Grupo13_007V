package com.TiendaRopa.ms_pedidos.Controller;

import com.TiendaRopa.ms_pedidos.DTO.EstadoDTO;
import com.TiendaRopa.ms_pedidos.DTO.PedidoDTO;
import com.TiendaRopa.ms_pedidos.Model.PedidoModel;
import com.TiendaRopa.ms_pedidos.Service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor

public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoModel>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoModel> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoModel>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<PedidoModel> crear(@Valid @RequestBody PedidoDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearDesdCarrito(productoDTO));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoModel> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoDTO estadoDTO) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estadoDTO));
    }

}
