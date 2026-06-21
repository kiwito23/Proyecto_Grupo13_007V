package com.TiendaRopa.ms_pedidos.Repositories;

import com.TiendaRopa.ms_pedidos.Model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {
    List<PedidoModel> findByUsuarioId(Long usuarioId);
    List<PedidoModel> findByEstado(PedidoModel.EstadoPedido estado);
}