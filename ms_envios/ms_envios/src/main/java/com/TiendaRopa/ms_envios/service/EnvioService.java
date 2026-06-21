package com.TiendaRopa.ms_envios.service;

import com.TiendaRopa.ms_envios.dto.EnvioDTO;
import com.TiendaRopa.ms_envios.exceptions.EnvioNotFoundException;
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

    // Obtiene todos los envios sin filtro
    public List<EnvioModel> obtenerTodos() {
        log.info("Obteniendo todos los envios");
        return envioRepository.findAll();
    }

    // Busca un envio por su ID
    // Si no existe lanza una excepción con mensaje descriptivo
    public EnvioModel obtenerPorId(Long id) {
        log.info("Buscando envio con id: {}", id);
        return envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNotFoundException("Envio no encontrado con id: " + id));
    }

    // Obtiene todos los envios de un usuario específico
    public List<EnvioModel> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando envios del usuario: {}", usuarioId);
        return envioRepository.findByUsuarioId(usuarioId);
    }

    // Obtiene el envio de un pedido específico
    public EnvioModel obtenerPorPedido(Long pedidoId) {
        log.info("Buscando envio del pedido: {}", pedidoId);
        return envioRepository.findByPedidoId(pedidoId);
    }

    public EnvioModel crear(EnvioDTO dto) {

        // Regla de negocio 1: No puede haber dos envios para el mismo pedido
        if (envioRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("Ya existe un envio para el pedido: " + dto.getPedidoId());
        }

        // Regla de negocio 2: Verifica que el pedido existe en ms-pedidos
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
        envio.setNumeroSeguimiento(dto.getNumeroSeguimiento());

        // Regla de negocio 3: Todo envio nuevo se crea en estado PREPARANDO
        envio.setEstado("PREPARANDO");

        return envioRepository.save(envio);
    }

    public EnvioModel actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del envio {} a {}", id, estado);
        EnvioModel envio = obtenerPorId(id);

        // Regla de negocio 4: El estado solo puede cambiar en orden logico
        // PREPARANDO → ENVIADO → ENTREGADO
        String estadoActual = envio.getEstado();
        if (estadoActual.equals("ENTREGADO")) {
            throw new RuntimeException("No se puede cambiar el estado de un envio ya entregado");
        }
        if (estadoActual.equals("ENVIADO") && estado.equals("PREPARANDO")) {
            throw new RuntimeException("No se puede volver al estado PREPARANDO desde ENVIADO");
        }

        envio.setEstado(estado);
        return envioRepository.save(envio);
    }

    public void eliminar(Long id) {
        log.info("Eliminando envio con id: {}", id);
        EnvioModel envio = obtenerPorId(id);

        // Regla de negocio 5: No se puede eliminar un envio que ya fue ENVIADO o ENTREGADO
        if (envio.getEstado().equals("ENVIADO") || envio.getEstado().equals("ENTREGADO")) {
            throw new RuntimeException("No se puede eliminar un envio en estado: " + envio.getEstado());
        }

        envioRepository.delete(envio);
    }
}