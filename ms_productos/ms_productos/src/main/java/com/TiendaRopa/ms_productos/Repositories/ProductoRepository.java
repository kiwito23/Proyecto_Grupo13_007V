package com.TiendaRopa.ms_productos.Repositories;

import com.TiendaRopa.ms_productos.Model.ProductosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductoRepository extends JpaRepository<ProductosModel, Long> {
    List<ProductosModel>findByCategoriaId(Long categoriaId);
    List<ProductosModel>findByActivoTrue();
}
