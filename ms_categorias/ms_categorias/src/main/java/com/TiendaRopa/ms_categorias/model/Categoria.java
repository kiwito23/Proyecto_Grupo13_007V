package com.TiendaRopa.ms_categorias.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categorias")
@Schema(description = "Entidad que representa una categoria de ropa")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la categoria", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Nombre de la categoria", example = "Ropa de Mujer")
    private String nombre;

    @Schema(description = "Descripcion de la categoria", example = "Prendas femeninas de todo tipo")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Estado de la categoria", example = "ACTIVO")
    private String estado = "ACTIVO";
}