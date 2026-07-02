package com.example.PRIMESHOP.excepcion;

/**
 * Se lanza cuando un intento de autenticación (login) falla porque el correo
 * no existe o la contraseña no coincide.
 * <p>
 * El {@link ManejadorGlobalExcepciones} la traduce siempre a un HTTP 401.
 */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Correo o contraseña incorrectos");
    }

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
