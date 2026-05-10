package com.TiendaRopa.ms_pedidos.Repositories;

import com.TiendaRopa.ms_pedidos.Model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {
    List<PedidoModel> findByUsuarioId(Long usuarioId);
    List<PedidoModel> findByEstado(PedidoModel.EstadoPedido estado);
}