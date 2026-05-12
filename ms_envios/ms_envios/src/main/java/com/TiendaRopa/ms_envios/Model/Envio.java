package com.TiendaRopa.ms_envios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El pedidoId es obligatorio")
    @Column(nullable = false)
    private Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    private Long usuarioId;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String estado = "PREPARANDO";

    private String numeroSeguimiento;
}