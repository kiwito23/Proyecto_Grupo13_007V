package com.TiendaRopa.ms_productos.ServiceTest;

import com.TiendaRopa.ms_productos.DTO.ProductosDTO;
import com.TiendaRopa.ms_productos.Exceptions.ProductoNotFoundException;
import com.TiendaRopa.ms_productos.Model.ProductosModel;
import com.TiendaRopa.ms_productos.Repositories.ProductoRepository;
import com.TiendaRopa.ms_productos.Service.ProductosService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductosServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    // RETURNS_DEEP_STUBS permite mockear la cadena fluida de WebClient
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientCategorias;

    @InjectMocks
    private ProductosService productosService;

    private ProductosModel producto;
    private ProductosDTO productoDTO;

    @BeforeEach
    void setUp() {
        // Producto de prueba reutilizable en todos los tests
        producto = new ProductosModel();
        producto.setId(1L);
        producto.setNombre("Polera Blanca");
        producto.setDescripcion("Polera algodón 100%");
        producto.setPrecio(new BigDecimal("12990.00"));
        producto.setStock(50);
        producto.setActivo(true);
        producto.setCategoriaId(2L);

        productoDTO = new ProductosDTO();
        productoDTO.setNombre("Polera Blanca");
        productoDTO.setDescripcion("Polera algodón 100%");
        productoDTO.setPrecio(new BigDecimal("12990.00"));
        productoDTO.setStock(50);
        productoDTO.setCategoriaId(2L);
    }

    // ----------------------------------------------------------------
    // listarActivos()
    // ----------------------------------------------------------------
    @Test
    void testListarActivos_retornaListaDeProductosActivos() {
        // GIVEN: el repositorio tiene un producto activo
        when(productoRepository.findByActivoTrue()).thenReturn(List.of(producto));

        // WHEN: se llama al servicio
        List<ProductosModel> resultado = productosService.listarActivos();

        // THEN: se retorna la lista con el producto esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    // ----------------------------------------------------------------
    // listarTodos()
    // ----------------------------------------------------------------
    @Test
    void testListarTodos_retornaTodosLosProductos() {
        // GIVEN: el repositorio retorna dos productos (activo e inactivo)
        ProductosModel inactivo = new ProductosModel();
        inactivo.setId(2L);
        inactivo.setActivo(false);
        when(productoRepository.findAll()).thenReturn(List.of(producto, inactivo));

        // WHEN
        List<ProductosModel> resultado = productosService.listarTodos();

        // THEN: se retornan ambos productos
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    // ----------------------------------------------------------------
    // obtenerProductosPorId() — caso encontrado
    // ----------------------------------------------------------------
    @Test
    void testObtenerProductosPorId_encontrado_retornaProducto() {
        // GIVEN: el repositorio encuentra el producto con id 1
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // WHEN
        ProductosModel resultado = productosService.obtenerProductosPorId(1L);

        // THEN: el producto retornado tiene los datos correctos
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Polera Blanca", resultado.getNombre());
    }

    // ----------------------------------------------------------------
    // obtenerProductosPorId() — caso NO encontrado → excepción
    // ----------------------------------------------------------------
    @Test
    void testObtenerProductosPorId_noEncontrado_lanzaExcepcion() {
        // GIVEN: el repositorio no encuentra el producto
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN: se espera que lance ProductoNotFoundException
        ProductoNotFoundException ex = assertThrows(
                ProductoNotFoundException.class,
                () -> productosService.obtenerProductosPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    // ----------------------------------------------------------------
    // listarPorCategoria()
    // ----------------------------------------------------------------
    @Test
    void testListarPorCategoria_retornaProductosDeLaCategoria() {
        // GIVEN: el repositorio filtra por categoriaId=2
        when(productoRepository.findByCategoriaId(2L)).thenReturn(List.of(producto));

        // WHEN
        List<ProductosModel> resultado = productosService.listarPorCategoria(2L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getCategoriaId());
        verify(productoRepository, times(1)).findByCategoriaId(2L);
    }

    // ----------------------------------------------------------------
    // desactivarProducto() — regla de negocio: DELETE lógico
    // ----------------------------------------------------------------
    @Test
    void testDesactivarProducto_ponActivoEnFalseYGuarda() {
        // GIVEN: el producto existe y está activo
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(ProductosModel.class))).thenReturn(producto);

        // WHEN: se desactiva el producto
        productosService.desactivarProducto(1L);

        // THEN: el producto queda inactivo y se llama a save
        assertFalse(producto.getActivo());
        verify(productoRepository, times(1)).save(producto);
    }

    // ----------------------------------------------------------------
    // crearProducto() — valida categoría vía WebClient y guarda
    // ----------------------------------------------------------------
    @Test
    void testCrearProducto_categoriaValida_guardaYRetornaProducto() {
        // GIVEN: WebClient responde OK (deep stubs retornan null en .block(), sin excepción)
        when(webClientCategorias.get()
                .uri(anyString(), any(Object[].class))
                .retrieve()
                .bodyToMono(Object.class))
                .thenReturn(Mono.just(new Object()));

        when(productoRepository.save(any(ProductosModel.class))).thenReturn(producto);

        // WHEN
        ProductosModel resultado = productosService.crearProducto(productoDTO);

        // THEN: el producto fue guardado correctamente
        assertNotNull(resultado);
        assertEquals("Polera Blanca", resultado.getNombre());
        verify(productoRepository, times(1)).save(any(ProductosModel.class));
    }

    // ----------------------------------------------------------------
    // crearProducto() — categoría inválida → lanza excepción
    // ----------------------------------------------------------------
    @Test
    void testCrearProducto_categoriaInvalida_lanzaExcepcion() {
        // GIVEN: WebClient lanza excepción (categoría no existe)
        when(webClientCategorias.get()
                .uri(anyString(), any(Object[].class))
                .retrieve()
                .bodyToMono(Object.class))
                .thenReturn(Mono.error(new RuntimeException("Categoría no encontrada")));

        // WHEN / THEN: se espera excepción de negocio
        assertThrows(
                ProductoNotFoundException.class,
                () -> productosService.crearProducto(productoDTO)
        );
        // El repositorio NO debe guardar nada si la categoría falló
        verify(productoRepository, never()).save(any());
    }
}
