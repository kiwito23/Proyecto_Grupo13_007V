package com.TiendaRopa.ms_pagos.repository;

import com.TiendaRopa.ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long usuarioId);
    List<Pago> findByEstado(String estado);
    Pago findByPedidoId(Long pedidoId);
}