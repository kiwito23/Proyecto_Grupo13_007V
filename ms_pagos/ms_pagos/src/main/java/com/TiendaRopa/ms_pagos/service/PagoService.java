package com.TiendaRopa.ms_pagos.service;

import com.TiendaRopa.ms_pagos.dto.PagoDTO;
import com.TiendaRopa.ms_pagos.model.Pago;
import com.TiendaRopa.ms_pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;
    private final WebClient webClientPedidos;

    // Obtiene todos los pagos sin filtro
    public List<Pago> obtenerTodos() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll();
    }

    // Busca un pago por su ID
    // Si no existe lanza una excepción con mensaje descriptivo
    public Pago obtenerPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
    }

    // Obtiene todos los pagos de un usuario específico
    public List<Pago> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando pagos del usuario: {}", usuarioId);
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    // Obtiene el pago de un pedido específico
    public Pago obtenerPorPedido(Long pedidoId) {
        log.info("Buscando pago del pedido: {}", pedidoId);
        return pagoRepository.findByPedidoId(pedidoId);
    }

    public Pago crear(PagoDTO dto) {

        // Regla de negocio 1: No puede haber dos pagos para el mismo pedido
        if (pagoRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("Ya existe un pago para el pedido: " + dto.getPedidoId());
        }

        // Regla de negocio 2: El monto debe ser mayor a 0
        if (dto.getMonto() <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }

        // Regla de negocio 3: Verifica que el pedido existe en ms-pedidos
        log.info("Verificando pedido {} en ms-pedidos", dto.getPedidoId());
        try {
            webClientPedidos.get()
                    .uri("/api/pedidos/{id}", dto.getPedidoId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Pedido {} verificado correctamente", dto.getPedidoId());
        } catch (Exception e) {
            log.error("Pedido no encontrado con id: {}", dto.getPedidoId());
            throw new RuntimeException("El pedido con id " + dto.getPedidoId() + " no existe");
        }

        log.info("Procesando pago para pedido: {}", dto.getPedidoId());
        Pago pago = new Pago();
        pago.setPedidoId(dto.getPedidoId());
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());

        // Regla de negocio 4: Todo pago nuevo se crea en estado PENDIENTE
        pago.setEstado("PENDIENTE");
        pago.setCodigoTransaccion(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return pagoRepository.save(pago);
    }

    public Pago actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del pago {} a {}", id, estado);
        Pago pago = obtenerPorId(id);

        // Regla de negocio 5: Solo se puede cancelar un pago en estado PENDIENTE
        if (estado.equals("CANCELADO") && !pago.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se puede cancelar un pago en estado PENDIENTE");
        }

        // No se puede modificar un pago ya cancelado
        if (pago.getEstado().equals("CANCELADO")) {
            throw new RuntimeException("No se puede modificar un pago cancelado");
        }

        pago.setEstado(estado);
        return pagoRepository.save(pago);
    }

    public void eliminar(Long id) {
        log.info("Eliminando pago con id: {}", id);
        Pago pago = obtenerPorId(id);

        // Regla de negocio 6: Solo se puede eliminar un pago en estado PENDIENTE
        if (!pago.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se puede eliminar un pago en estado PENDIENTE");
        }

        pagoRepository.deleteById(id);
    }
}