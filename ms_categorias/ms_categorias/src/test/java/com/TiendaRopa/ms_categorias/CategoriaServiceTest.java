package com.TiendaRopa.ms_categorias;
import com.TiendaRopa.ms_categorias.model.Categoria;
import com.TiendaRopa.ms_categorias.repository.CategoriaRepository;
import com.TiendaRopa.ms_categorias.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
     @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    // Test 1 - Obtener todas las categorias
    @Test
    void testObtenerTodas() {
        // Given
        Categoria cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNombre("Ropa Mujer");
        cat1.setEstado("ACTIVO");

        Categoria cat2 = new Categoria();
        cat2.setId(2L);
        cat2.setNombre("Ropa Hombre");
        cat2.setEstado("ACTIVO");

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        // When
        List<Categoria> resultado = categoriaService.obtenerTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    // Test 2 - Obtener categoria por ID existente
    @Test
    void testObtenerPorIdExistente() {
        // Given
        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setNombre("Ropa Mujer");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));

        // When
        Categoria resultado = categoriaService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ropa Mujer", resultado.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    // Test 3 - Obtener categoria por ID no existente
    @Test
    void testObtenerPorIdNoExistente() {
        // Given
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(RuntimeException.class, () -> {
            categoriaService.obtenerPorId(99L);
        });
        verify(categoriaRepository, times(1)).findById(99L);
    }

    // Test 4 - Obtener categorias activas
    @Test
    void testObtenerActivas() {
        // Given
        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setNombre("Ropa Mujer");
        cat.setEstado("ACTIVO");

        when(categoriaRepository.findByEstado("ACTIVO")).thenReturn(Arrays.asList(cat));

        // When
        List<Categoria> resultado = categoriaService.obtenerActivas();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getEstado());
        verify(categoriaRepository, times(1)).findByEstado("ACTIVO");
    }

   // Test 5 - Eliminar categoria (borrado logico)
@Test
void testEliminarCategoria() {
    // Given - categoria con estado ACTIVO
    Categoria cat = new Categoria();
    cat.setId(1L);
    cat.setNombre("Ropa Mujer");
    cat.setEstado("ACTIVO");

    when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
    when(categoriaRepository.save(any(Categoria.class))).thenReturn(cat);

    // When
    categoriaService.eliminar(1L);

    // Then - verifica que se guardó con estado INACTIVO (borrado logico)
    verify(categoriaRepository, times(1)).save(cat);
    assertEquals("INACTIVO", cat.getEstado());
    }
}


