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

    public List<Talla> obtenerTodas() {
        log.info("Obteniendo todas las tallas");
        return tallaRepository.findAll();
    }

    public Talla obtenerPorId(Long id) {
        log.info("Buscando talla con id: {}", id);
        return tallaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talla no encontrada con id: " + id));
    }

    public List<Talla> obtenerActivas() {
        log.info("Obteniendo tallas activas");
        return tallaRepository.findByEstado("ACTIVO");
    }

    public Talla crear(TallaDTO dto) {
        log.info("Creando talla: {}", dto.getNombre());
        Talla talla = new Talla();
        talla.setNombre(dto.getNombre());
        talla.setDescripcion(dto.getDescripcion());
        return tallaRepository.save(talla);
    }

    public Talla actualizar(Long id, TallaDTO dto) {
        log.info("Actualizando talla con id: {}", id);
        Talla talla = obtenerPorId(id);
        talla.setNombre(dto.getNombre());
        talla.setDescripcion(dto.getDescripcion());
        return tallaRepository.save(talla);
    }

    public void eliminar(Long id) {
        log.info("Eliminando talla con id: {}", id);
        obtenerPorId(id);
        tallaRepository.deleteById(id);
    }
}