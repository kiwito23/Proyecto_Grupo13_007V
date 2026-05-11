package com.TiendaRopa.ms_tallas.repository;

import com.TiendaRopa.ms_tallas.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Long> {
    List<Talla> findByEstado(String estado);
    Talla findByNombre(String nombre);
}