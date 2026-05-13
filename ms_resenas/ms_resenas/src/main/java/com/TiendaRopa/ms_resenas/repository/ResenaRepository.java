package com.TiendaRopa.ms_resenas.repository;

import com.TiendaRopa.ms_resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
    List<Resena> findByUsuarioId(Long usuarioId);
    List<Resena> findByEstado(String estado);
}