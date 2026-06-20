package com.TiendaRopa.ms_inventario.Repositories;

import com.TiendaRopa.ms_inventario.Model.MovimientosInventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientosInventarioRepository extends JpaRepository<MovimientosInventarioModel, Long> {
    List<MovimientosInventarioModel> findByInventarioId(Long inventarioId);



}
