package com.TiendaRopa.ms_productos.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;


@Entity
@Table(name = "producto")
@Data

public class ProductosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column (nullable = false)
    private Integer stock;

    @Column (nullable = false)
    private Boolean activo = true;

    @Column (name = "categoria_id", nullable = false)
    private Long categoriaId;


}
