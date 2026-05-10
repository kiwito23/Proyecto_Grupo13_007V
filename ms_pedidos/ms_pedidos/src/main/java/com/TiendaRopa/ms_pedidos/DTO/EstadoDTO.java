package com.TiendaRopa.ms_pedidos.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoDTO {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

}
