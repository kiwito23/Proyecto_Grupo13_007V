package com.TiendaRopa.ms_inventario.Repositories;

import com.TiendaRopa.ms_inventario.Model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioModel, Long> {
    Optional<InventarioModel> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);
}


