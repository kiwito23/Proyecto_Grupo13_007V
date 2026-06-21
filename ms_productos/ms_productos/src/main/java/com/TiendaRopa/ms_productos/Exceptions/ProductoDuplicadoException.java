package com.TiendaRopa.ms_productos.Exceptions;

public class ProductoDuplicadoException extends RuntimeException {
    public ProductoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
