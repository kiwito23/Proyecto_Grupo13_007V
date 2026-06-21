package com.TiendaRopa.ms_inventario.Exceptions;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
