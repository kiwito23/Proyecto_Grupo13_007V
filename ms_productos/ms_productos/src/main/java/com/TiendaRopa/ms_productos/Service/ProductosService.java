package com.TiendaRopa.ms_productos.Service;
 
import com.TiendaRopa.ms_productos.DTO.ProductosDTO;
import com.TiendaRopa.ms_productos.Model.ProductosModel;
import com.TiendaRopa.ms_productos.Repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;



@Service
@RequiredArgsConstructor
@Slf4j
public class ProductosService {

    private final ProductoRepository productoRepository;
    private final WebClient webClientCategorias;

    public List<ProductosModel> listarActivos() {
        log.info("Listando productos activos");
        return productoRepository.findByActivoTrue();
    }

    public List<ProductosModel> listarTodos() {
        log.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    public ProductosModel obtenerProductosPorId(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado con id: {}", id);
                    return new RuntimeException("Producto no encontrado con id: " + id);
                });
    }

    public ProductosModel crearProducto(ProductosDTO productoDTO) {
        log.info("Creando producto: {}", productoDTO.getNombre());

        // Valida que la categoría existe en ms-categorias
        validarCategoriaExiste(productoDTO.getCategoriaId());

        ProductosModel producto = new ProductosModel();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setActivo(true);
        producto.setCategoriaId(productoDTO.getCategoriaId());

        ProductosModel guardado = productoRepository.save(producto);
        log.info("Producto creado con id: {}", guardado.getId());
        return guardado;
    }

    public ProductosModel actualizarProducto(Long id, ProductosDTO productoDTO) {
        log.info("Actualizando producto con id: {}", id);
        validarCategoriaExiste(productoDTO.getCategoriaId());

        ProductosModel producto = obtenerProductosPorId(id);
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setCategoriaId(productoDTO .getCategoriaId());
        return productoRepository.save(producto);
    }

    public void desactivarProducto(Long id) {
        log.info("Desactivando producto con id: {}", id);
        ProductosModel producto = obtenerProductosPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
        log.info("Producto desactivado con id: {}", id);
    }

    public List<ProductosModel> listarPorCategoria(Long categoriaId) {
        log.info("Listando productos de categoría id: {}", categoriaId);
        return productoRepository.findByCategoriaId(categoriaId);
    }

    private void validarCategoriaExiste(Long categoriaId) {
        try {
            log.info("Validando categoría id: {} en ms-categorias", categoriaId);
            webClientCategorias.get()
                    .uri("/api/categorias/{id}", categoriaId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Categoría id: {} validada correctamente", categoriaId);
        } catch (Exception e) {
            log.error("Categoría no encontrada en ms-categorias con id: {}", categoriaId);
            throw new RuntimeException("La categoría con id " + categoriaId + " no existe");
        }
    }

}
