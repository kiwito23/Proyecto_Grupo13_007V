package com.TiendaRopa.ms_pedidos.Controller;

import com.TiendaRopa.ms_pedidos.DTO.EstadoDTO;
import com.TiendaRopa.ms_pedidos.DTO.PedidoDTO;
import com.TiendaRopa.ms_pedidos.Model.PedidoModel;
import com.TiendaRopa.ms_pedidos.Service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Orquestación del flujo de compra: usuario, carrito, inventario, pago y envío")
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Listar todos los pedidos",
        description = "Devuelve el historial completo de pedidos registrados en el sistema")
    @GetMapping
    public ResponseEntity<List<PedidoModel>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @Operation(summary = "Obtener pedido por ID",
        description = "Devuelve el detalle de un pedido específico")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoModel> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @Operation(summary = "Listar pedidos de un usuario",
        description = "Devuelve el historial de pedidos asociados a un usuario específico")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoModel>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Crear pedido a partir del carrito",
        description = "Orquesta el flujo completo de compra entre 5 microservicios: " +
            "1) valida al usuario en ms_usuarios, " +
            "2) obtiene el contenido del carrito en ms_carrito, " +
            "3) descuenta el stock correspondiente en ms_inventario, " +
            "4) procesa el pago en ms_pagos, " +
            "5) genera el envío en ms_envios, y finalmente vacía el carrito. " +
            "Si alguna validación remota falla, el pedido no se crea.")
    @PostMapping
    public ResponseEntity<PedidoModel> crear(@Valid @RequestBody PedidoDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearDesdCarrito(productoDTO));
    }

    @Operation(summary = "Actualizar estado del pedido",
        description = "Cambia el estado de un pedido (por ejemplo a PROCESANDO, ENVIADO o ENTREGADO). " +
            "No permite modificar pedidos que ya estén en estado ENTREGADO o CANCELADO.")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoModel> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoDTO estadoDTO) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estadoDTO));
    }

}