package com.TiendaRopa.ms_categorias.repository;

import com.TiendaRopa.ms_categorias.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByEstado(String estado);
    Categoria findByNombre(String nombre);
}
