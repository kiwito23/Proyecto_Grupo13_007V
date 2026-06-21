package com.TiendaRopa.ms_tallas.repository;

import com.TiendaRopa.ms_tallas.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TallaRepository extends JpaRepository<Talla, Long> {
    List<Talla> findByEstado(String estado);
    Talla findByNombre(String nombre);

    // Verifica si existe una talla con ese nombre
    boolean existsByNombre(String nombre);

    // Verifica si existe una talla con ese nombre pero con un ID diferente
    // Se usa para validar duplicados al actualizar
    boolean existsByNombreAndIdNot(String nombre, Long id);
}