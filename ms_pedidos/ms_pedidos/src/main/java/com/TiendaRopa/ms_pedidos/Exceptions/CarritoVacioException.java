package com.TiendaRopa.ms_pedidos.Exceptions;

public class CarritoVacioException extends RuntimeException {
    public CarritoVacioException(String mensaje) {
        super(mensaje);
    }
}
