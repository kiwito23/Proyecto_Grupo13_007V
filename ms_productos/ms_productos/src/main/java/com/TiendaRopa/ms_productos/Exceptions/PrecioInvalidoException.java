package com.TiendaRopa.ms_productos.Exceptions;

public class PrecioInvalidoException extends RuntimeException {
    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
