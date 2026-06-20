package com.TiendaRopa.ms_carrito.ServiceTest;

import com.TiendaRopa.ms_carrito.Exceptions.CarritoNotFoundException;
import com.TiendaRopa.ms_carrito.Model.CarritoModel;
import com.TiendaRopa.ms_carrito.Model.ItemCarritoModel;
import com.TiendaRopa.ms_carrito.Repositories.CarritoRepository;
import com.TiendaRopa.ms_carrito.Repositories.ItemCarritoRepository;
import com.TiendaRopa.ms_carrito.Services.CarritoService;

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
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ItemCarritoRepository itemCarritoRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientUsuarios;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientProductos;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClientTallas;

    @InjectMocks
    private CarritoService carritoService;

    private CarritoModel carrito;
    private ItemCarritoModel item;

    @BeforeEach
    void setUp() {
        carrito = new CarritoModel();
        carrito.setId(1L);
        carrito.setUsuarioId(10L);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setActivo(true);

        item = new ItemCarritoModel();
        item.setId(1L);
        item.setCarrito(carrito);
        item.setProductoId(5L);
        item.setCantidad(2);
        item.setPrecioUnitario(new BigDecimal("12990.00"));
        item.setTallaId(3L);
    }

    // ----------------------------------------------------------------
    // obtenerItems() — retorna items del carrito del usuario
    // ----------------------------------------------------------------
    @Test
    void testObtenerItems_retornaItemsDelCarrito() {
        // GIVEN: el carrito del usuario existe y tiene un item
        when(carritoRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.findByCarritoId(1L)).thenReturn(List.of(item));

        // WHEN
        List<ItemCarritoModel> resultado = carritoService.obtenerItems(10L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getProductoId());
        verify(itemCarritoRepository, times(1)).findByCarritoId(1L);
    }

    // ----------------------------------------------------------------
    // vaciarCarrito() — elimina todos los items del carrito
    // ----------------------------------------------------------------
    @Test
    void testVaciarCarrito_eliminaTodosLosItems() {
        // GIVEN
        when(carritoRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.findByCarritoId(1L)).thenReturn(List.of(item));
        doNothing().when(itemCarritoRepository).deleteAll(anyList());

        // WHEN
        carritoService.vaciarCarrito(10L);

        // THEN: se elimina la lista completa de items
        verify(itemCarritoRepository, times(1)).deleteAll(List.of(item));
    }

    // ----------------------------------------------------------------
    // eliminarItem() — item existe → se elimina
    // ----------------------------------------------------------------
    @Test
    void testEliminarItem_itemExiste_seElimina() {
        // GIVEN
        when(carritoRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(1L, 5L)).thenReturn(Optional.of(item));
        doNothing().when(itemCarritoRepository).delete(item);

        // WHEN
        carritoService.eliminarItem(10L, 5L);

        // THEN
        verify(itemCarritoRepository, times(1)).delete(item);
    }

    // ----------------------------------------------------------------
    // eliminarItem() — item NO existe → lanza excepción
    // ----------------------------------------------------------------
    @Test
    void testEliminarItem_itemNoExiste_lanzaExcepcion() {
        // GIVEN: el producto 99 no está en el carrito del usuario
        when(carritoRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(Optional.of(carrito));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(1L, 99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                CarritoNotFoundException.class,
                () -> carritoService.eliminarItem(10L, 99L)
        );
        verify(itemCarritoRepository, never()).delete(any());
    }
}
