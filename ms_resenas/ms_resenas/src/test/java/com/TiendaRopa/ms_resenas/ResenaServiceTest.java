package com.TiendaRopa.ms_resenas;
import com.TiendaRopa.ms_resenas.model.Resena;
import com.TiendaRopa.ms_resenas.repository.ResenaRepository;
import com.TiendaRopa.ms_resenas.service.ResenaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResenaServiceTest {
    @Mock
    private ResenaRepository resenaRepository;

    @InjectMocks
    private ResenaService resenaService;

    // Test 1 - Obtener todas las reseñas
    @Test
    void testObtenerTodas() {
        // Given
        Resena resena1 = new Resena();
        resena1.setId(1L);
        resena1.setComentario("Muy bueno");
        resena1.setCalificacion(5);
        resena1.setRecomendado(true);

        Resena resena2 = new Resena();
        resena2.setId(2L);
        resena2.setComentario("Regular");
        resena2.setCalificacion(3);
        resena2.setRecomendado(false);

        when(resenaRepository.findAll()).thenReturn(Arrays.asList(resena1, resena2));

        // When
        List<Resena> resultado = resenaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(resenaRepository, times(1)).findAll();
    }

    // Test 2 - Obtener reseña por ID existente
    @Test
    void testObtenerPorIdExistente() {
        // Given
        Resena resena = new Resena();
        resena.setId(1L);
        resena.setComentario("Excelente producto");
        resena.setCalificacion(5);
        resena.setRecomendado(true);

        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));

        // When
        Resena resultado = resenaService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Excelente producto", resultado.getComentario());
        verify(resenaRepository, times(1)).findById(1L);
    }

    // Test 3 - Obtener reseña por ID no existente
@Test
void testObtenerPorIdNoExistente() {
    // Given
    when(resenaRepository.findById(99L)).thenReturn(Optional.empty());

    // When - Then
    assertThrows(RuntimeException.class, () -> {
        resenaService.obtenerPorId(99L);
    });
    verify(resenaRepository, times(1)).findById(99L);
}

   // Test 4 - Actualizar reseña
@Test
void testActualizarResena() {
    // Given
    Resena resenaExistente = new Resena();
    resenaExistente.setId(1L);
    resenaExistente.setComentario("Comentario viejo");
    resenaExistente.setCalificacion(3);

    when(resenaRepository.save(resenaExistente)).thenReturn(resenaExistente);

    // When
    resenaExistente.setComentario("Comentario nuevo");
    resenaExistente.setCalificacion(5);
    Resena resultado = resenaRepository.save(resenaExistente);

    // Then
    assertNotNull(resultado);
    assertEquals("Comentario nuevo", resultado.getComentario());
    assertEquals(5, resultado.getCalificacion());
    verify(resenaRepository, times(1)).save(resenaExistente);
}
    // Test 5 - Obtener reseñas recomendadas
    @Test
    void testObtenerRecomendadas() {
        // Given
        Resena resena1 = new Resena();
        resena1.setId(1L);
        resena1.setRecomendado(true);
        resena1.setComentario("Excelente");

        Resena resena2 = new Resena();
        resena2.setId(2L);
        resena2.setRecomendado(true);
        resena2.setComentario("Muy bueno");

        when(resenaRepository.findTop5ByRecomendado(true, PageRequest.of(0, 5)))
            .thenReturn(Arrays.asList(resena1, resena2));

        // When
        List<Resena> resultado = resenaService.obtenerRecomendadas(true);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).getRecomendado());
        verify(resenaRepository, times(1))
            .findTop5ByRecomendado(true, PageRequest.of(0, 5));
    }

}
