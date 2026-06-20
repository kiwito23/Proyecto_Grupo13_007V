package com.TiendaRopa.ms_pagos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos")
@Schema(description = "Entidad que representa un pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del pago", example = "1")
    private Long id;

    @NotNull(message = "El pedidoId es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del pedido asociado", example = "1")
    private Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del usuario", example = "1")
    private Long usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.1", message = "El monto debe ser mayor a 0")
    @Column(nullable = false)
    @Schema(description = "Monto del pago", example = "50000.0")
    private Double monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Metodo de pago", example = "TARJETA")
    private String metodoPago;

    @Column(nullable = false)
    @Schema(description = "Estado del pago", example = "PENDIENTE")
    private String estado = "PENDIENTE";

    @Schema(description = "Codigo de transaccion", example = "ABC12345")
    private String codigoTransaccion;
}