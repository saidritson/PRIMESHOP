package com.example.PRIMESHOP.excepcion;

import java.util.Map;

/**
 * Extiende {@link ErrorRespuestaDto} agregando el detalle campo → mensaje
 * cuando la falla proviene de Bean Validation (@NotNull, @Size, @Email, etc.).
 *
 * Ejemplo de salida:
 * <pre>
 * {
 *   "timestamp": "2026-07-01T10:15:30",
 *   "status": 400,
 *   "error": "Error de validación",
 *   "message": "Uno o más campos no son válidos",
 *   "path": "/api/productos",
 *   "errores": {
 *     "nombre": "El nombre del producto es obligatorio",
 *     "precio": "El precio debe ser mayor a 0"
 *   }
 * }
 * </pre>
 */
public class ErrorValidacionRespuestaDto extends ErrorRespuestaDto {

    private Map<String, String> errores;

    public ErrorValidacionRespuestaDto() {
    }

    public ErrorValidacionRespuestaDto(int status, String error, String message, String path,
                                        Map<String, String> errores) {
        super(status, error, message, path);
        this.errores = errores;
    }

    public Map<String, String> getErrores() {
        return errores;
    }

    public void setErrores(Map<String, String> errores) {
        this.errores = errores;
    }
}
