package com.TiendaRopa.ms_pagos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PagoDTO {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.1", message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    private String metodoPago;
}