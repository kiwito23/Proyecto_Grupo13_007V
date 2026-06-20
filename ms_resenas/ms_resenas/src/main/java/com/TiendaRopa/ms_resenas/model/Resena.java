package com.TiendaRopa.ms_resenas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "resenas")
@Schema(description = "Entidad que representa una reseña de producto")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la reseña", example = "1")
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del usuario que hace la reseña", example = "1")
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del producto reseñado", example = "1")
    private Long productoId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    @Schema(description = "Calificacion del producto del 1 al 5", example = "5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    @Schema(description = "Comentario de la reseña", example = "Excelente producto")
    private String comentario;

    @Column(nullable = false)
    @Schema(description = "Estado de la reseña", example = "ACTIVO")
    private String estado = "ACTIVO";

    @Column(nullable = false)
    @Schema(description = "Indica si la reseña es recomendada", example = "true")
    private Boolean recomendado = false;
}