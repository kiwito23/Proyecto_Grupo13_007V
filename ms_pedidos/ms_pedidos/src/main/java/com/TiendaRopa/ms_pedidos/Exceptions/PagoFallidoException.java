package com.TiendaRopa.ms_pedidos.Exceptions;

public class PagoFallidoException extends RuntimeException {
    public PagoFallidoException(String mensaje) {
        super(mensaje);
    }
}
