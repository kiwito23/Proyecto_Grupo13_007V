package com.TiendaRopa.ms_categorias.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoriaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
}