package com.TiendaRopa.ms_envios;
import com.TiendaRopa.ms_envios.Model.EnvioModel;
import com.TiendaRopa.ms_envios.exceptions.EnvioNotFoundException;
import com.TiendaRopa.ms_envios.repository.EnvioRepository;
import com.TiendaRopa.ms_envios.service.EnvioService;
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
public class EnvioServiceTest {
    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private org.springframework.web.reactive.function.client.WebClient webClientPedidos;

    @InjectMocks
    private EnvioService envioService;

    // Test 1 - Obtener todos los envios
    @Test
    void testObtenerTodos() {
        // Given
        EnvioModel envio1 = new EnvioModel();
        envio1.setId(1L);
        envio1.setDireccion("Calle 1");
        envio1.setCiudad("Santiago");

        EnvioModel envio2 = new EnvioModel();
        envio2.setId(2L);
        envio2.setDireccion("Calle 2");
        envio2.setCiudad("Valparaiso");

        when(envioRepository.findAll()).thenReturn(Arrays.asList(envio1, envio2));

        // When
        List<EnvioModel> resultado = envioService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(envioRepository, times(1)).findAll();
    }

    // Test 2 - Obtener envio por ID existente
    @Test
    void testObtenerPorIdExistente() {
        // Given
        EnvioModel envio = new EnvioModel();
        envio.setId(1L);
        envio.setDireccion("Calle 1");
        envio.setCiudad("Santiago");

        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));

        // When
        EnvioModel resultado = envioService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Santiago", resultado.getCiudad());
        verify(envioRepository, times(1)).findById(1L);
    }

    // Test 3 - Obtener envio por ID no existente
    @Test
    void testObtenerPorIdNoExistente() {
        // Given
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(EnvioNotFoundException.class, () -> {
            envioService.obtenerPorId(99L);
        });
        verify(envioRepository, times(1)).findById(99L);
    }

    // Test 4 - Obtener envios por usuario
    @Test
    void testObtenerPorUsuario() {
        // Given
        EnvioModel envio = new EnvioModel();
        envio.setId(1L);
        envio.setUsuarioId(1L);
        envio.setCiudad("Santiago");

        when(envioRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(envio));

        // When
        List<EnvioModel> resultado = envioService.obtenerPorUsuario(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuarioId());
        verify(envioRepository, times(1)).findByUsuarioId(1L);
    }

    // Test 5 - Actualizar estado del envio
@Test
void testActualizarEstado() {
    // Given - estado inicial PREPARANDO
    EnvioModel envio = new EnvioModel();
    envio.setId(1L);
    envio.setEstado("PREPARANDO");

    when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
    when(envioRepository.save(envio)).thenReturn(envio);

    // When - cambia a ENVIADO
    EnvioModel resultado = envioService.actualizarEstado(1L, "ENVIADO");

    // Then
    assertNotNull(resultado);
    assertEquals("ENVIADO", resultado.getEstado());
    verify(envioRepository, times(1)).save(envio);
    }
}
