package com.TiendaRopa.ms_inventario.Repositories;

import com.TiendaRopa.ms_inventario.Model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<InventarioModel, Long> {
    Optional<InventarioModel> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);
}


