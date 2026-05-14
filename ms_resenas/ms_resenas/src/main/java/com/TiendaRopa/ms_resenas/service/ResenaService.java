package com.TiendaRopa.ms_resenas.service;

import com.TiendaRopa.ms_resenas.dto.ResenaDTO;
import com.TiendaRopa.ms_resenas.model.Resena;
import com.TiendaRopa.ms_resenas.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);
    private final ResenaRepository resenaRepository;

    public List<Resena> obtenerTodas() {
        log.info("Obteniendo todas las resenas");
        return resenaRepository.findAll();
    }

    public Resena obtenerPorId(Long id) {
        log.info("Buscando resena con id: {}", id);
        return resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada con id: " + id));
    }

    public List<Resena> obtenerPorProducto(Long productoId) {
        log.info("Buscando resenas del producto: {}", productoId);
        return resenaRepository.findByProductoId(productoId);
    }

    public List<Resena> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando resenas del usuario: {}", usuarioId);
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    public Resena crear(ResenaDTO dto) {
        log.info("Creando resena para producto: {}", dto.getProductoId());
        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setProductoId(dto.getProductoId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        return resenaRepository.save(resena);
    }

    public Resena actualizar(Long id, ResenaDTO dto) {
        log.info("Actualizando resena con id: {}", id);
        Resena resena = obtenerPorId(id);
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        return resenaRepository.save(resena);
    }

    public void eliminar(Long id) {
        log.info("Eliminando resena con id: {}", id);
        obtenerPorId(id);
        resenaRepository.deleteById(id);
    }
}