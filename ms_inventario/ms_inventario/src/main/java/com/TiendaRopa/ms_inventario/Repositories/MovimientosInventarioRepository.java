package com.TiendaRopa.ms_inventario.Repositories;

import com.TiendaRopa.ms_inventario.Model.MovimientosInventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimientosInventarioRepository extends JpaRepository<MovimientosInventarioModel, Long> {
    List<MovimientosInventarioModel> findByInventarioId(Long inventarioId);



}
