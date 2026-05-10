package com.TiendaRopa.ms_productos.Controller;

import com.TiendaRopa.ms_productos.DTO.ProductosDTO;
import com.TiendaRopa.ms_productos.Model.ProductosModel;
import com.TiendaRopa.ms_productos.Service.ProductosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductosController {

    private final ProductosService productoService;

    @GetMapping
    public ResponseEntity<List<ProductosModel>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ProductosModel>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductosModel > obtenerProductosPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductosPorId(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductosModel>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
    }

    @PostMapping
    public ResponseEntity<ProductosModel> crear(@Valid @RequestBody ProductosDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(productoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductosModel> actualizar(@PathVariable Long id, @Valid @RequestBody ProductosDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }


}
