package com.TiendaRopa.ms_inventario.Exceptions;

public class InventarioNotFoundException extends RuntimeException {
    public InventarioNotFoundException(String mensaje) {
        super(mensaje);
    }
}