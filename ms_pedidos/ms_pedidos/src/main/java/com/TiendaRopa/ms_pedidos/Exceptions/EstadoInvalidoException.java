package com.TiendaRopa.ms_pedidos.Exceptions;

public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
