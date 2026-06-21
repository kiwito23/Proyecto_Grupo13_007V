package com.TiendaRopa.ms_pedidos.Exceptions;

public class PedidoDuplicadoException extends RuntimeException {
    public PedidoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
