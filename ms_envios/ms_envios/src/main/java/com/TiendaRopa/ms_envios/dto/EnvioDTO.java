package com.TiendaRopa.ms_envios.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EnvioDTO {

    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;
}