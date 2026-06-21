package com.TiendaRopa.ms_resenas.repository;

import com.TiendaRopa.ms_resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
    List<Resena> findByUsuarioId(Long usuarioId);
    List<Resena> findByEstado(String estado);
    List<Resena> findByRecomendado(Boolean recomendado);
    List<Resena> findTop5ByRecomendado(Boolean recomendado, Pageable pageable);

    // Verifica si un usuario ya tiene una reseña para un producto específico
    // Se usa para evitar reseñas duplicadas del mismo usuario al mismo producto
    boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
}