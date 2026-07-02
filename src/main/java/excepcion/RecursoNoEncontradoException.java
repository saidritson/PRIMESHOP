package com.example.PRIMESHOP.excepcion;

/**
 * Se lanza cuando se solicita una entidad (Producto, Categoria, Usuario, Pedido, etc.)
 * que no existe en la base de datos.
 * <p>
 * El {@link ManejadorGlobalExcepciones} la traduce siempre a un HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /** Atajo típico: RecursoNoEncontradoException("Producto", id) → "Producto no encontrado con id: 5" */
    public RecursoNoEncontradoException(String recurso, Object identificador) {
        super(recurso + " no encontrado con id: " + identificador);
    }
}
