package com.TiendaRopa.ms_envios.service;

import com.TiendaRopa.ms_envios.dto.EnvioDTO;
import com.TiendaRopa.ms_envios.Model.EnvioModel;
import com.TiendaRopa.ms_envios.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnvioService {

private final EnvioRepository envioRepository;
private final WebClient webClientPedidos;

    public List<EnvioModel> obtenerTodos() {
        log.info("Obteniendo todos los envios");
        return envioRepository.findAll();
    }

    public EnvioModel obtenerPorId(Long id) {
        log.info("Buscando envio con id: {}", id);
        return envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNotFoundException("Envio no encontrado con id: " + id));
    }

    public List<EnvioModel> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando envios del usuario: {}", usuarioId);
        return envioRepository.findByUsuarioId(usuarioId);
    }

    public EnvioModel obtenerPorPedido(Long pedidoId) {
        log.info("Buscando envio del pedido: {}", pedidoId);
        return envioRepository.findByPedidoId(pedidoId);
    }

    public EnvioModel crear(EnvioDTO dto) {
        log.info("Verificando pedido {} en ms-pedidos", dto.getPedidoId());
        try {
            webClientPedidos.get()
                    .uri("/api/pedidos/{id}", dto.getPedidoId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Pedido {} verificado correctamente", dto.getPedidoId());
        } catch (Exception e) {
            log.error("Pedido no encontrado con id: {}", dto.getPedidoId());
            throw new RuntimeException("El pedido con id " + dto.getPedidoId() + " no existe");
        }

        log.info("Creando envio para pedido: {}", dto.getPedidoId());
        EnvioModel envio = new EnvioModel();
        envio.setPedidoId(dto.getPedidoId());
        envio.setUsuarioId(dto.getUsuarioId());
        envio.setDireccion(dto.getDireccion());
        envio.setCiudad(dto.getCiudad());
        envio.setNumeroSeguimiento(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return envioRepository.save(envio);
    }

    public EnvioModel actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del envio {} a {}", id, estado);
        EnvioModel envio = obtenerPorId(id);
        envio.setEstado(estado);
        return envioRepository.save(envio);
    }

    public void eliminar(Long id) {
        log.info("Eliminando envio con id: {}", id);
        EnvioModel envio = obtenerPorId(id);
        envioRepository.delete(envio);
    }

}
