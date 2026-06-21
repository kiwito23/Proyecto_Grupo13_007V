package com.TiendaRopa.ms_productos.Exceptions;

public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(String mensaje) {
        super(mensaje);
    }
}
