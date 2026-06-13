package com.TiendaRopa.ms_pagos;
import com.TiendaRopa.ms_pagos.model.Pago;
import com.TiendaRopa.ms_pagos.repository.PagoRepository;
import com.TiendaRopa.ms_pagos.service.PagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {
     @Mock
    private PagoRepository pagoRepository;

    @Mock
    private org.springframework.web.reactive.function.client.WebClient webClientPedidos;

    @InjectMocks
    private PagoService pagoService;

    // Test 1 - Obtener todos los pagos
    @Test
    void testObtenerTodos() {
        // Given
        Pago pago1 = new Pago();
        pago1.setId(1L);
        pago1.setMonto(50000.0);
        pago1.setEstado("APROBADO");

        Pago pago2 = new Pago();
        pago2.setId(2L);
        pago2.setMonto(30000.0);
        pago2.setEstado("PENDIENTE");

        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago1, pago2));

        // When
        List<Pago> resultado = pagoService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pagoRepository, times(1)).findAll();
    }

    // Test 2 - Obtener pago por ID existente
    @Test
    void testObtenerPorIdExistente() {
        // Given
        Pago pago = new Pago();
        pago.setId(1L);
        pago.setMonto(50000.0);
        pago.setEstado("APROBADO");

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        // When
        Pago resultado = pagoService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("APROBADO", resultado.getEstado());
        verify(pagoRepository, times(1)).findById(1L);
    }

    // Test 3 - Obtener pago por ID no existente
    @Test
    void testObtenerPorIdNoExistente() {
        // Given
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(RuntimeException.class, () -> {
            pagoService.obtenerPorId(99L);
        });
        verify(pagoRepository, times(1)).findById(99L);
    }

    // Test 4 - Obtener pagos por usuario
    @Test
    void testObtenerPorUsuario() {
        // Given
        Pago pago = new Pago();
        pago.setId(1L);
        pago.setUsuarioId(1L);
        pago.setMonto(50000.0);

        when(pagoRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(pago));

        // When
        List<Pago> resultado = pagoService.obtenerPorUsuario(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuarioId());
        verify(pagoRepository, times(1)).findByUsuarioId(1L);
    }

    // Test 5 - Actualizar estado del pago
    @Test
    void testActualizarEstado() {
        // Given
        Pago pago = new Pago();
        pago.setId(1L);
        pago.setEstado("PENDIENTE");

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(pago)).thenReturn(pago);

        // When
        Pago resultado = pagoService.actualizarEstado(1L, "APROBADO");

        // Then
        assertNotNull(resultado);
        assertEquals("APROBADO", resultado.getEstado());
        verify(pagoRepository, times(1)).save(pago);
    }


}
