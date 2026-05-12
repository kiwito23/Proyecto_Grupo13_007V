package com.TiendaRopa.ms_pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El pedidoId es obligatorio")
    @Column(nullable = false)
    private Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.1", message = "El monto debe ser mayor a 0")
    @Column(nullable = false)
    private Double monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    private String codigoTransaccion;
}