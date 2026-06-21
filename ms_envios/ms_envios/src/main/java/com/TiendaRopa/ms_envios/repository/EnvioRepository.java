package com.TiendaRopa.ms_envios.repository;

import com.TiendaRopa.ms_envios.Model.EnvioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvioRepository extends JpaRepository<EnvioModel, Long> {
    List<EnvioModel> findByUsuarioId(Long usuarioId);
    List<EnvioModel> findByEstado(String estado);
    EnvioModel findByPedidoId(Long pedidoId);

    // Verifica si ya existe un envio para ese pedido
    // Se usa para evitar crear dos envios para el mismo pedido
    boolean existsByPedidoId(Long pedidoId);
}