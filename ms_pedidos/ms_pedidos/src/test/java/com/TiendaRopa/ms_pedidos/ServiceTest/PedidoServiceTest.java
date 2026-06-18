package com.TiendaRopa.ms_pedidos.ServiceTest;

import com.TiendaRopa.ms_pedidos.DTO.EstadoDTO;
import com.TiendaRopa.ms_pedidos.Exceptions.PedidoNotFoundException;
import com.TiendaRopa.ms_pedidos.Model.PedidoModel;
import com.TiendaRopa.ms_pedidos.Repositories.PedidoRepository;
import com.TiendaRopa.ms_pedidos.Service.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientUsuarios;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientInventario;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientCarrito;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientEnvios;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientPagos;

    @InjectMocks
    private PedidoService pedidoService;

    private PedidoModel pedido;

    @BeforeEach
    void setUp() {
        pedido = new PedidoModel();
        pedido.setId(1L);
        pedido.setUsuarioId(10L);
        pedido.setEstado(PedidoModel.EstadoPedido.PENDIENTE);
        pedido.setTotal(new BigDecimal("25990.00"));
        pedido.setDireccionEntrega("Av. Principal 123, Santiago");
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());
    }

    // ----------------------------------------------------------------
    // listarTodos()
    // ----------------------------------------------------------------
    @Test
    void testListarTodos_retornaTodosLosPedidos() {
        // GIVEN
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        // WHEN
        List<PedidoModel> resultado = pedidoService.listarTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    // ----------------------------------------------------------------
    // listarPorUsuario()
    // ----------------------------------------------------------------
    @Test
    void testListarPorUsuario_retornaPedidosDelUsuario() {
        // GIVEN
        when(pedidoRepository.findByUsuarioId(10L)).thenReturn(List.of(pedido));

        // WHEN
        List<PedidoModel> resultado = pedidoService.listarPorUsuario(10L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
    }

    // ----------------------------------------------------------------
    // obtenerPorId() — encontrado
    // ----------------------------------------------------------------
    @Test
    void testObtenerPorId_encontrado_retornaPedido() {
        // GIVEN
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // WHEN
        PedidoModel resultado = pedidoService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(PedidoModel.EstadoPedido.PENDIENTE, resultado.getEstado());
    }

    // ----------------------------------------------------------------
    // obtenerPorId() — no encontrado → excepción
    // ----------------------------------------------------------------
    @Test
    void testObtenerPorId_noEncontrado_lanzaExcepcion() {
        // GIVEN
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        PedidoNotFoundException ex = assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    // ----------------------------------------------------------------
    // actualizarEstado() — transición válida: PENDIENTE → CONFIRMADO
    // ----------------------------------------------------------------
    @Test
    void testActualizarEstado_transicionValida_cambiaEstado() {
        // GIVEN
        pedido.setEstado(PedidoModel.EstadoPedido.PENDIENTE);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(PedidoModel.class))).thenReturn(pedido);

        EstadoDTO dto = new EstadoDTO();
        dto.setEstado("CONFIRMADO");

        // WHEN
        PedidoModel resultado = pedidoService.actualizarEstado(1L, dto);

        // THEN: el estado cambió a CONFIRMADO
        assertEquals(PedidoModel.EstadoPedido.CONFIRMADO, resultado.getEstado());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    // ----------------------------------------------------------------
    // actualizarEstado() — pedido ENTREGADO no se puede modificar
    // ----------------------------------------------------------------
    @Test
    void testActualizarEstado_pedidoEntregado_lanzaExcepcion() {
        // GIVEN: el pedido ya fue entregado (estado final)
        pedido.setEstado(PedidoModel.EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        EstadoDTO dto = new EstadoDTO();
        dto.setEstado("CANCELADO");

        // WHEN / THEN: no se puede cambiar estado de un pedido ENTREGADO
        assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.actualizarEstado(1L, dto)
        );
        verify(pedidoRepository, never()).save(any());
    }

    // ----------------------------------------------------------------
    // actualizarEstado() — estado inválido → excepción
    // ----------------------------------------------------------------
    @Test
    void testActualizarEstado_estadoInvalido_lanzaExcepcion() {
        // GIVEN: el estado enviado no existe en el enum EstadoPedido
        pedido.setEstado(PedidoModel.EstadoPedido.PENDIENTE);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        EstadoDTO dto = new EstadoDTO();
        dto.setEstado("ESTADO_INVENTADO");

        // WHEN / THEN
        PedidoNotFoundException ex = assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.actualizarEstado(1L, dto)
        );
        assertTrue(ex.getMessage().contains("Estado inválido"));
    }
}

