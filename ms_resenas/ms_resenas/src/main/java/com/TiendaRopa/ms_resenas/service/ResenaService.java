package com.TiendaRopa.ms_resenas.service;

import com.TiendaRopa.ms_resenas.dto.ResenaDTO;
import com.TiendaRopa.ms_resenas.model.Resena;
import com.TiendaRopa.ms_resenas.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final WebClient webClientProductos;

    // Obtiene todas las reseñas sin filtro
    public List<Resena> obtenerTodas() {
        log.info("Obteniendo todas las resenas");
        return resenaRepository.findAll();
    }

    // Busca una reseña por su ID
    // Si no existe lanza una excepción con mensaje descriptivo
    public Resena obtenerPorId(Long id) {
        log.info("Buscando resena con id: {}", id);
        return resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada con id: " + id));
    }

    // Obtiene todas las reseñas de un producto específico
    public List<Resena> obtenerPorProducto(Long productoId) {
        log.info("Buscando resenas del producto: {}", productoId);
        return resenaRepository.findByProductoId(productoId);
    }

    // Obtiene todas las reseñas de un usuario específico
    public List<Resena> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando resenas del usuario: {}", usuarioId);
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    public Resena crear(ResenaDTO dto) {
        log.info("Creando resena para producto: {}", dto.getProductoId());

        // Regla de negocio 1: Un usuario solo puede reseñar un producto una vez
        if (resenaRepository.existsByUsuarioIdAndProductoId(dto.getUsuarioId(), dto.getProductoId())) {
            throw new RuntimeException("El usuario " + dto.getUsuarioId() + 
                " ya tiene una reseña para el producto " + dto.getProductoId());
        }

        // Regla de negocio 2: La calificacion debe estar entre 1 y 5
        if (dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
            throw new RuntimeException("La calificacion debe estar entre 1 y 5");
        }

        // Verifica que el producto existe en ms-productos
        log.info("Verificando producto {} en ms-productos", dto.getProductoId());
        try {
            webClientProductos.get()
                    .uri("/api/productos/{id}", dto.getProductoId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Producto {} verificado correctamente", dto.getProductoId());
        } catch (Exception e) {
            log.error("Producto no encontrado con id: {}", dto.getProductoId());
            throw new RuntimeException("El producto con id " + dto.getProductoId() + " no existe");
        }

        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setProductoId(dto.getProductoId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());

        // Regla de negocio 3: Toda reseña nueva se crea con estado ACTIVO por defecto
        resena.setEstado("ACTIVO");
        resena.setRecomendado(false);

        return resenaRepository.save(resena);
    }

    public Resena actualizar(Long id, ResenaDTO dto) {
        log.info("Actualizando resena con id: {}", id);
        Resena resena = obtenerPorId(id);

        // Regla de negocio 4: La calificacion debe estar entre 1 y 5
        if (dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
            throw new RuntimeException("La calificacion debe estar entre 1 y 5");
        }

        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        return resenaRepository.save(resena);
    }

    public void eliminar(Long id) {
        log.info("Eliminando resena con id: {}", id);
        Resena resena = obtenerPorId(id);

        // Regla de negocio 5: No se elimina fisicamente de la base de datos
        // Se cambia el estado a INACTIVO para mantener el historial
        if (resena.getEstado().equals("INACTIVO")) {
            throw new RuntimeException("La resena ya está inactiva");
        }

        resena.setEstado("INACTIVO");
        resenaRepository.save(resena);
    }

    // Obtiene las top 5 reseñas recomendadas
    public List<Resena> obtenerRecomendadas(Boolean recomendado) {
        log.info("Obteniendo 5 resenas con recomendado: {}", recomendado);
        return resenaRepository.findTop5ByRecomendado(recomendado, PageRequest.of(0, 5));
    }
}