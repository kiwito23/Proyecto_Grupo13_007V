package com.TiendaRopa.ms_inventario.ServiceTest;

import com.TiendaRopa.ms_inventario.DTO.MovimientosInventarioDTO;
import com.TiendaRopa.ms_inventario.Exceptions.InventarioNotFoundException;
import com.TiendaRopa.ms_inventario.Model.InventarioModel;
import com.TiendaRopa.ms_inventario.Model.MovimientosInventarioModel;
import com.TiendaRopa.ms_inventario.Repositories.InventarioRepository;
import com.TiendaRopa.ms_inventario.Repositories.MovimientosInventarioRepository;
import com.TiendaRopa.ms_inventario.Services.InventarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private MovimientosInventarioRepository movimientosInventarioRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientProductos;

    @InjectMocks
    private InventarioService inventarioService;

    private InventarioModel inventario;

    @BeforeEach
    void setUp() {
        inventario = new InventarioModel();
        inventario.setId(1L);
        inventario.setProductoId(10L);
        inventario.setStockActual(100);
        inventario.setStockMinimo(10);
        inventario.setUltimaActualizacion(LocalDateTime.now());
    }

    // ----------------------------------------------------------------
    // obtenerInventariosConDetalles()
    // ----------------------------------------------------------------
    @Test
    void testObtenerInventariosConDetalles_retornaListaCompleta() {
        // GIVEN
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));

        // WHEN
        List<InventarioModel> resultado = inventarioService.obtenerInventariosConDetalles();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    // ----------------------------------------------------------------
    // obtenerPorProductoId() — encontrado
    // ----------------------------------------------------------------
    @Test
    void testObtenerPorProductoId_encontrado_retornaInventario() {
        // GIVEN
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        // WHEN
        InventarioModel resultado = inventarioService.obtenerPorProductoId(10L);

        // THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.getProductoId());
        assertEquals(100, resultado.getStockActual());
    }

    // ----------------------------------------------------------------
    // obtenerPorProductoId() — no encontrado → excepción
    // ----------------------------------------------------------------
    @Test
    void testObtenerPorProductoId_noEncontrado_lanzaExcepcion() {
        // GIVEN
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        InventarioNotFoundException ex = assertThrows(
                InventarioNotFoundException.class,
                () -> inventarioService.obtenerPorProductoId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    // ----------------------------------------------------------------
    // registrarMovimiento() — ENTRADA → aumenta stock
    // ----------------------------------------------------------------
    @Test
    void testRegistrarMovimiento_entrada_aumentaStock() {
        // GIVEN
        int stockInicial = inventario.getStockActual(); // 100
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventario);
        when(movimientosInventarioRepository.save(any(MovimientosInventarioModel.class)))
                .thenReturn(new MovimientosInventarioModel());

        MovimientosInventarioDTO dto = new MovimientosInventarioDTO();
        dto.setTipoMovimiento("ENTRADA");
        dto.setCantidad(20);
        dto.setMotivo("Reposición de stock");

        // WHEN
        InventarioModel resultado = inventarioService.registrarMovimiento(10L, dto);

        // THEN: el stock aumentó en 20
        assertEquals(stockInicial + 20, resultado.getStockActual());
        verify(inventarioRepository, times(1)).save(inventario);
    }

    // ----------------------------------------------------------------
    // registrarMovimiento() — SALIDA → disminuye stock
    // ----------------------------------------------------------------
    @Test
    void testRegistrarMovimiento_salida_disminuyeStock() {
        // GIVEN
        inventario.setStockActual(100);
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(inventario);
        when(movimientosInventarioRepository.save(any(MovimientosInventarioModel.class)))
                .thenReturn(new MovimientosInventarioModel());

        MovimientosInventarioDTO dto = new MovimientosInventarioDTO();
        dto.setTipoMovimiento("SALIDA");
        dto.setCantidad(30);
        dto.setMotivo("Pedido #1");

        // WHEN
        InventarioModel resultado = inventarioService.registrarMovimiento(10L, dto);

        // THEN: el stock disminuyó en 30
        assertEquals(70, resultado.getStockActual());
    }

    // ----------------------------------------------------------------
    // registrarMovimiento() — SALIDA con stock insuficiente → excepción
    // ----------------------------------------------------------------
    @Test
    void testRegistrarMovimiento_stockInsuficiente_lanzaExcepcion() {
        // GIVEN: stock actual = 100, pero se piden 200
        inventario.setStockActual(100);
        when(inventarioRepository.findByProductoId(10L)).thenReturn(Optional.of(inventario));

        MovimientosInventarioDTO dto = new MovimientosInventarioDTO();
        dto.setTipoMovimiento("SALIDA");
        dto.setCantidad(200);
        dto.setMotivo("Pedido imposible");

        // WHEN / THEN: debe lanzar excepción de stock insuficiente
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> inventarioService.registrarMovimiento(10L, dto)
        );
        assertTrue(ex.getMessage().contains("stock"));
        // No se guarda nada si hay error
        verify(inventarioRepository, never()).save(any());
    }
}

