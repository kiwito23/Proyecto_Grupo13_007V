package com.TiendaRopa.ms_usuarios.Exceptions;

/**
 * Se lanza cuando se intenta crear un usuario con un email que ya existe.
 * Antes esto usaba incorrectamente UsuarioNotFoundException ("no encontrado")
 * para un caso que en realidad es "ya existe" (duplicado). Se mapea a 409 CONFLICT
 * en el GlobalExceptionHandler, no a 404 ni a 400.
 */
public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
