package com.TiendaRopa.ms_envios.repository;

import com.TiendaRopa.ms_envios.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByUsuarioId(Long usuarioId);
    List<Envio> findByEstado(String estado);
    Envio findByPedidoId(Long pedidoId);
}