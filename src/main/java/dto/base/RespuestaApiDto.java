package com.example.PRIMESHOP.dto.base;

/**
 * Envoltura estándar y opcional para respuestas exitosas de la API, de forma
 * simétrica al {@code ErrorRespuestaDto} usado para los errores.
 * <pre>
 * {
 *   "exito": true,
 *   "mensaje": "Producto creado exitosamente",
 *   "datos": { ...DtoSalida... }
 * }
 * </pre>
 *
 * @param <T> el tipo de {@link DtoSalida} (o lista de ellos) que se está devolviendo
 */
public class RespuestaApiDto<T> {

    private boolean exito;
    private String mensaje;
    private T datos;

    public RespuestaApiDto() {
    }

    private RespuestaApiDto(boolean exito, String mensaje, T datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public static <T> RespuestaApiDto<T> ok(T datos) {
        return new RespuestaApiDto<>(true, null, datos);
    }

    public static <T> RespuestaApiDto<T> ok(String mensaje, T datos) {
        return new RespuestaApiDto<>(true, mensaje, datos);
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getDatos() {
        return datos;
    }

    public void setDatos(T datos) {
        this.datos = datos;
    }
}
