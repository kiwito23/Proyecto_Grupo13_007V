package com.TiendaRopa.ms_inventario.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MovimientosInventarioDTO {

    @NotNull (message = "El ID del inventario no puede ser nulo")
    private Long inventarioId;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento; // "entrada" o "salida"

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotBlank (message = "El motivo del movimiento es obligatorio")
    private String motivo;

    @NotBlank (message = "La fecha del movimiento es obligatoria")
    private String fechaMovimiento;

}
