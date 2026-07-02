package com.example.PRIMESHOP.excepcion;

/**
 * Se lanza cuando una operación es sintácticamente válida pero viola una regla
 * de negocio (ej. eliminar una categoría con productos asociados, stock insuficiente
 * para completar un pedido, etc.).
 * <p>
 * El {@link ManejadorGlobalExcepciones} la traduce siempre a un HTTP 422 (Unprocessable Entity).
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
