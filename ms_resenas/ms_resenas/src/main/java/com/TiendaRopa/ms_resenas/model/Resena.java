package com.TiendaRopa.ms_resenas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    @Column(nullable = false)
    private Long productoId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;

    @Column(nullable = false)
    private String estado = "ACTIVO";
}