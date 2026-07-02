package com.example.PRIMESHOP.excepcion;

/**
 * Se lanza al intentar crear/actualizar un recurso que viola una restricción
 * de unicidad de negocio (ej. correo ya registrado, nombre de categoría repetido).
 * <p>
 * El {@link ManejadorGlobalExcepciones} la traduce siempre a un HTTP 409 (Conflict).
 */
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
