package com.TiendaRopa.ms_productos.Repositories;

import com.TiendaRopa.ms_productos.Model.ProductosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductosModel, Long> {
    List<ProductosModel>findByCategoriaId(Long categoriaId);
    List<ProductosModel>findByActivoTrue();
}
