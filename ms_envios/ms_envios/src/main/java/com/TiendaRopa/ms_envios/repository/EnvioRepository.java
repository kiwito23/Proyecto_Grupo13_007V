package com.TiendaRopa.ms_envios.repository;

import com.TiendaRopa.ms_envios.Model.EnvioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<EnvioModel, Long> {
    List<EnvioModel> findByUsuarioId(Long usuarioId);
    List<EnvioModel> findByEstado(String estado);
    EnvioModel findByPedidoId(Long pedidoId);
}