package com.TiendaRopa.ms_tallas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tallas")
@Schema(description = "Entidad que representa una talla de ropa")
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la talla", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Nombre de la talla", example = "M")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripcion de la talla", example = "Talla mediana")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Estado de la talla", example = "ACTIVO")
    private String estado = "ACTIVO";
}