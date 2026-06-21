package com.TiendaRopa.ms_pedidos.Exceptions;

public class UsuarioNoExisteException extends RuntimeException {
    public UsuarioNoExisteException(String mensaje) {
        super(mensaje);
    }
}
