package com.TiendaRopa.ms_envios.service;

import com.TiendaRopa.ms_envios.dto.EnvioDTO;
import com.TiendaRopa.ms_envios.model.Envio;
import com.TiendaRopa.ms_envios.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private static final Logger log = LoggerFactory.getLogger(EnvioService.class);
    private final EnvioRepository envioRepository;

    public List<Envio> obtenerTodos() {
        log.info("Obteniendo todos los envios");
        return envioRepository.findAll();
    }

    public Envio obtenerPorId(Long id) {
        log.info("Buscando envio con id: {}", id);
        return envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado con id: " + id));
    }

    public List<Envio> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando envios del usuario: {}", usuarioId);
        return envioRepository.findByUsuarioId(usuarioId);
    }

    public Envio obtenerPorPedido(Long pedidoId) {
        log.info("Buscando envio del pedido: {}", pedidoId);
        return envioRepository.findByPedidoId(pedidoId);
    }

    public Envio crear(EnvioDTO dto) {
        log.info("Creando envio para pedido: {}", dto.getPedidoId());
        Envio envio = new Envio();
        envio.setPedidoId(dto.getPedidoId());
        envio.setUsuarioId(dto.getUsuarioId());
        envio.setDireccion(dto.getDireccion());
        envio.setCiudad(dto.getCiudad());
        envio.setNumeroSeguimiento(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return envioRepository.save(envio);
    }

    public Envio actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del envio {} a {}", id, estado);
        Envio envio = obtenerPorId(id);
        envio.setEstado(estado);
        return envioRepository.save(envio);
    }

    public void eliminar(Long id) {
        log.info("Eliminando envio con id: {}", id);
        obtenerPorId(id);
        envioRepository.deleteById(id);
    }
}