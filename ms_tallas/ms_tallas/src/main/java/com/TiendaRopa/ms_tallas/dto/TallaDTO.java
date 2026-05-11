package com.TiendaRopa.ms_tallas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TallaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}