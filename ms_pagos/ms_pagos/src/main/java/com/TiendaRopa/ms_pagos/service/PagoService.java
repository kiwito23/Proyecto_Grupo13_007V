package com.TiendaRopa.ms_pagos.service;

import com.TiendaRopa.ms_pagos.dto.PagoDTO;
import com.TiendaRopa.ms_pagos.model.Pago;
import com.TiendaRopa.ms_pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository pagoRepository;

    public List<Pago> obtenerTodos() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll();
    }

    public Pago obtenerPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
    }

    public List<Pago> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando pagos del usuario: {}", usuarioId);
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    public Pago obtenerPorPedido(Long pedidoId) {
        log.info("Buscando pago del pedido: {}", pedidoId);
        return pagoRepository.findByPedidoId(pedidoId);
    }

    public Pago crear(PagoDTO dto) {
        log.info("Procesando pago para pedido: {}", dto.getPedidoId());
        Pago pago = new Pago();
        pago.setPedidoId(dto.getPedidoId());
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado("APROBADO");
        pago.setCodigoTransaccion(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return pagoRepository.save(pago);
    }

    public Pago actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del pago {} a {}", id, estado);
        Pago pago = obtenerPorId(id);
        pago.setEstado(estado);
        return pagoRepository.save(pago);
    }

    public void eliminar(Long id) {
        log.info("Eliminando pago con id: {}", id);
        obtenerPorId(id);
        pagoRepository.deleteById(id);
    }
}