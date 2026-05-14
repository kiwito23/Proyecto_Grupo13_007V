package com.TiendaRopa.ms_pedidos.Exceptions;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String mensaje) {
        super(mensaje);
    }
}