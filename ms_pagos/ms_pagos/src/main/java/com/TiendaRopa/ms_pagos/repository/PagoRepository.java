package com.TiendaRopa.ms_pagos.repository;

import com.TiendaRopa.ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long usuarioId);
    List<Pago> findByEstado(String estado);
    Pago findByPedidoId(Long pedidoId);

    // Verifica si ya existe un pago para ese pedido
    // Se usa para evitar crear dos pagos para el mismo pedido
    boolean existsByPedidoId(Long pedidoId);
}