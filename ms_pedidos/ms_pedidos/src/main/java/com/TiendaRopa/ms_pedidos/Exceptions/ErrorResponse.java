package com.TiendaRopa.ms_pedidos.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private LocalDateTime fecha;
    private int status;
    private String error;
    private String mensaje;
    private String ruta;
    
}