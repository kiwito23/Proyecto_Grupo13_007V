package com.TiendaRopa.ms_categorias.service;

import com.TiendaRopa.ms_categorias.dto.CategoriaDTO;
import com.TiendaRopa.ms_categorias.model.Categoria;
import com.TiendaRopa.ms_categorias.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaService.class);
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas() {
        log.info("Obteniendo todas las categorias");
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        log.info("Buscando categoria con id: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
    }

    public List<Categoria> obtenerActivas() {
        log.info("Obteniendo categorias activas");
        return categoriaRepository.findByEstado("ACTIVO");
    }

    public Categoria crear(CategoriaDTO dto) {
        log.info("Creando categoria: {}", dto.getNombre());
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, CategoriaDTO dto) {
        log.info("Actualizando categoria con id: {}", id);
        Categoria categoria = obtenerPorId(id);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        log.info("Eliminando categoria con id: {}", id);
        obtenerPorId(id);
        categoriaRepository.deleteById(id);
    }
}