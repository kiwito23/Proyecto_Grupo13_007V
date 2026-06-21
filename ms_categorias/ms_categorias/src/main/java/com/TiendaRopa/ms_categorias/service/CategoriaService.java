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

    // Obtiene todas las categorias sin filtro
    public List<Categoria> obtenerTodas() {
        log.info("Obteniendo todas las categorias");
        return categoriaRepository.findAll();
    }

    // Busca una categoria por su ID
    // Si no existe lanza una excepción con mensaje descriptivo
    public Categoria obtenerPorId(Long id) {
        log.info("Buscando categoria con id: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
    }

    // Obtiene solo las categorias con estado ACTIVO
    public List<Categoria> obtenerActivas() {
        log.info("Obteniendo categorias activas");
        return categoriaRepository.findByEstado("ACTIVO");
    }

    public Categoria crear(CategoriaDTO dto) {
        log.info("Creando categoria: {}", dto.getNombre());

        // Regla de negocio 1: No se permite crear una categoria con un nombre que ya existe
        // Evita duplicados en la base de datos
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una categoria con el nombre: " + dto.getNombre());
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        // Regla de negocio 2: Toda categoria nueva se crea con estado ACTIVO por defecto
        categoria.setEstado("ACTIVO");

        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, CategoriaDTO dto) {
        log.info("Actualizando categoria con id: {}", id);

        // Primero verifica que la categoria existe
        Categoria categoria = obtenerPorId(id);

        // Regla de negocio 3: No se puede actualizar el nombre si ya existe en otra categoria
        // El IdNot excluye la categoria actual de la busqueda para no compararse consigo misma
        if (categoriaRepository.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new RuntimeException("Ya existe otra categoria con el nombre: " + dto.getNombre());
        }

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        log.info("Eliminando categoria con id: {}", id);

        // Primero verifica que la categoria existe
        Categoria categoria = obtenerPorId(id);

        // Regla de negocio 4: No se elimina fisicamente de la base de datos
        // Se cambia el estado a INACTIVO para mantener el historial
        // Esto se llama "borrado logico"
        if (categoria.getEstado().equals("INACTIVO")) {
            throw new RuntimeException("La categoria ya está inactiva");
        }

        categoria.setEstado("INACTIVO");
        categoriaRepository.save(categoria);
    }

    public Categoria desactivar(Long id) {
        log.info("Desactivando categoria con id: {}", id);

        Categoria categoria = obtenerPorId(id);

        // Regla de negocio 5: No se puede desactivar una categoria que ya está inactiva
        if (categoria.getEstado().equals("INACTIVO")) {
            throw new RuntimeException("La categoria ya está inactiva");
        }

        categoria.setEstado("INACTIVO");
        return categoriaRepository.save(categoria);
    }
}