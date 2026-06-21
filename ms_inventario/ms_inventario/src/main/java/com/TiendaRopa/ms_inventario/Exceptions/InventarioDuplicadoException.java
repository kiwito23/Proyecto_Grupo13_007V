package com.TiendaRopa.ms_inventario.Exceptions;

public class InventarioDuplicadoException extends RuntimeException {
    public InventarioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
