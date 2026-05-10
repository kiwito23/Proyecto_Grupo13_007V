package com.TiendaRopa.ms_carrito.Repositories;

import com.TiendaRopa.ms_carrito.Model.ItemCarritoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarritoModel, Long>{
    List<ItemCarritoModel> findByCarritoId(Long carritoId);
    Optional<ItemCarritoModel> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
}
