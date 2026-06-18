package com.TiendaRopa.ms_productos.Controller;

import com.TiendaRopa.ms_productos.DTO.ProductosDTO;
import com.TiendaRopa.ms_productos.Model.ProductosModel;
import com.TiendaRopa.ms_productos.Service.ProductosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión del catálogo de productos")
public class ProductosController {

    private final ProductosService productoService;

    @Operation(summary = "Listar productos activos",
        description = "Devuelve únicamente los productos que están disponibles para la venta")
    @GetMapping
    public ResponseEntity<List<ProductosModel>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @Operation(summary = "Listar todos los productos",
        description = "Devuelve todos los productos, incluyendo los desactivados")
    @GetMapping("/todos")
    public ResponseEntity<List<ProductosModel>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(summary = "Obtener producto por ID",
        description = "Devuelve el detalle de un producto específico")
    @GetMapping("/{id}")
    public ResponseEntity<ProductosModel > obtenerProductosPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductosPorId(id));
    }

    @Operation(summary = "Listar productos por categoría",
        description = "Devuelve los productos asociados a una categoría específica")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductosModel>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
    }

    @Operation(summary = "Crear producto",
        description = "Registra un nuevo producto en el catálogo, validando que la categoría exista")
    @PostMapping
    public ResponseEntity<ProductosModel> crear(@Valid @RequestBody ProductosDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(productoDTO));
    }

    @Operation(summary = "Actualizar producto",
        description = "Modifica los datos de un producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProductosModel> actualizar(@PathVariable Long id, @Valid @RequestBody ProductosDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @Operation(summary = "Desactivar producto",
        description = "Realiza un borrado lógico del producto (no lo elimina físicamente de la base de datos)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }

}