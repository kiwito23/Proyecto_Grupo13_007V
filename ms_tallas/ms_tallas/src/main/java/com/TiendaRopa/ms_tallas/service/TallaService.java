package com.TiendaRopa.ms_tallas.service;

import com.TiendaRopa.ms_tallas.dto.TallaDTO;
import com.TiendaRopa.ms_tallas.model.Talla;
import com.TiendaRopa.ms_tallas.repository.TallaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TallaService {

    private static final Logger log = LoggerFactory.getLogger(TallaService.class);
    private final TallaRepository tallaRepository;

    // Obtiene todas las tallas sin filtro
    public List<Talla> obtenerTodas() {
        log.info("Obteniendo todas las tallas");
        return tallaRepository.findAll();
    }

    // Busca una talla por su ID
    // Si no existe lanza una excepción con mensaje descriptivo
    public Talla obtenerPorId(Long id) {
        log.info("Buscando talla con id: {}", id);
        return tallaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talla no encontrada con id: " + id));
    }

    // Obtiene solo las tallas con estado ACTIVO
    public List<Talla> obtenerActivas() {
        log.info("Obteniendo tallas activas");
        return tallaRepository.findByEstado("ACTIVO");
    }

    public Talla crear(TallaDTO dto) {
        log.info("Creando talla: {}", dto.getNombre());

        // Regla de negocio 1: No se permite crear una talla con un nombre que ya existe
        // Evita duplicados como tener dos tallas "M"
        if (tallaRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una talla con el nombre: " + dto.getNombre());
        }

        Talla talla = new Talla();
        talla.setNombre(dto.getNombre());
        talla.setDescripcion(dto.getDescripcion());

        // Regla de negocio 2: Toda talla nueva se crea con estado ACTIVO por defecto
        talla.setEstado("ACTIVO");

        return tallaRepository.save(talla);
    }

    public Talla actualizar(Long id, TallaDTO dto) {
        log.info("Actualizando talla con id: {}", id);

        // Primero verifica que la talla existe
        Talla talla = obtenerPorId(id);

        // Regla de negocio 3: No se puede actualizar el nombre si ya existe en otra talla
        if (tallaRepository.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new RuntimeException("Ya existe otra talla con el nombre: " + dto.getNombre());
        }

        talla.setNombre(dto.getNombre());
        talla.setDescripcion(dto.getDescripcion());
        return tallaRepository.save(talla);
    }

    public void eliminar(Long id) {
        log.info("Eliminando talla con id: {}", id);

        // Primero verifica que la talla existe
        Talla talla = obtenerPorId(id);

        // Regla de negocio 4: No se elimina fisicamente de la base de datos
        // Se cambia el estado a INACTIVO para mantener el historial
        // Esto se llama "borrado logico"
        if (talla.getEstado().equals("INACTIVO")) {
            throw new RuntimeException("La talla ya está inactiva");
        }

        talla.setEstado("INACTIVO");
        tallaRepository.save(talla);
    }

    public Talla desactivar(Long id) {
        log.info("Desactivando talla con id: {}", id);

        Talla talla = obtenerPorId(id);

        // Regla de negocio 5: No se puede desactivar una talla que ya está inactiva
        if (talla.getEstado().equals("INACTIVO")) {
            throw new RuntimeException("La talla ya está inactiva");
        }

        talla.setEstado("INACTIVO");
        return tallaRepository.save(talla);
    }
}