package com.TiendaRopa.ms_carrito.Exceptions;

public class CarritoNotFoundException extends RuntimeException {
    public CarritoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
