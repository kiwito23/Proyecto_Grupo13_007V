package com.TiendaRopa.ms_resenas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResenaDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
}