package com.TiendaRopa.ms_tallas;
import com.TiendaRopa.ms_tallas.model.Talla;
import com.TiendaRopa.ms_tallas.repository.TallaRepository;
import com.TiendaRopa.ms_tallas.service.TallaService;
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
public class TallaServiceTest {
    @Mock
    private TallaRepository tallaRepository;

    @InjectMocks
    private TallaService tallaService;

    // Test 1 - Obtener todas las tallas
    @Test
    void testObtenerTodas() {
        // Given
        Talla talla1 = new Talla();
        talla1.setId(1L);
        talla1.setNombre("S");
        talla1.setEstado("ACTIVO");

        Talla talla2 = new Talla();
        talla2.setId(2L);
        talla2.setNombre("M");
        talla2.setEstado("ACTIVO");

        when(tallaRepository.findAll()).thenReturn(Arrays.asList(talla1, talla2));

        // When
        List<Talla> resultado = tallaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(tallaRepository, times(1)).findAll();
    }

    // Test 2 - Obtener talla por ID existente
    @Test
    void testObtenerPorIdExistente() {
        // Given
        Talla talla = new Talla();
        talla.setId(1L);
        talla.setNombre("M");

        when(tallaRepository.findById(1L)).thenReturn(Optional.of(talla));

        // When
        Talla resultado = tallaService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("M", resultado.getNombre());
        verify(tallaRepository, times(1)).findById(1L);
    }

    // Test 3 - Obtener talla por ID no existente
    @Test
    void testObtenerPorIdNoExistente() {
        // Given
        when(tallaRepository.findById(99L)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(RuntimeException.class, () -> {
            tallaService.obtenerPorId(99L);
        });
        verify(tallaRepository, times(1)).findById(99L);
    }

    // Test 4 - Obtener tallas activas
    @Test
    void testObtenerActivas() {
        // Given
        Talla talla = new Talla();
        talla.setId(1L);
        talla.setNombre("L");
        talla.setEstado("ACTIVO");

        when(tallaRepository.findByEstado("ACTIVO")).thenReturn(Arrays.asList(talla));

        // When
        List<Talla> resultado = tallaService.obtenerActivas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getEstado());
        verify(tallaRepository, times(1)).findByEstado("ACTIVO");
    }

    // Test 5 - Eliminar talla
    @Test
    void testEliminarTalla() {
        // Given
        Talla talla = new Talla();
        talla.setId(1L);
        talla.setNombre("XL");

        when(tallaRepository.findById(1L)).thenReturn(Optional.of(talla));
        doNothing().when(tallaRepository).deleteById(1L);

        // When
        tallaService.eliminar(1L);

        // Then
        verify(tallaRepository, times(1)).deleteById(1L);
    }

}
