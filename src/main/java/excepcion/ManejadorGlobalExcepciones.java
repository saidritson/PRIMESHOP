package com.example.PRIMESHOP.excepcion;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Manejo centralizado de excepciones para la capa de API REST.
 * <p>
 * Se restringe a controladores anotados con {@link RestController} (con
 * {@code annotations = RestController.class}) para no interferir con los
 * controladores MVC/Thymeleaf existentes (paquetes {@code controlador} y
 * {@code com.example.PRIMESHOP.controller}), que siguen manejando sus errores
 * con {@code RedirectAttributes} + flash messages y páginas HTML.
 * <p>
 * Contrato: toda respuesta de error de la API es un {@link ErrorRespuestaDto}
 * (o su subclase {@link ErrorValidacionRespuestaDto}) en formato JSON.
 * Nunca se expone el stacktrace al cliente; se registra únicamente en el log
 * del servidor mediante {@link #log}.
 */
@RestControllerAdvice(annotations = RestController.class)
public class ManejadorGlobalExcepciones {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    // ── 404: entidad no encontrada ──────────────────────────────────────────

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarNoEncontrado(RecursoNoEncontradoException ex,
                                                                  HttpServletRequest req) {
        return construirRespuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), req);
    }

    /** Compatibilidad con servicios existentes que aún lanzan NoSuchElementException. */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarNoSuchElement(NoSuchElementException ex,
                                                                   HttpServletRequest req) {
        return construirRespuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), req);
    }

    // ── 401: credenciales inválidas ─────────────────────────────────────────

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarCredencialesInvalidas(CredencialesInvalidasException ex,
                                                                           HttpServletRequest req) {
        return construirRespuesta(HttpStatus.UNAUTHORIZED, "No autorizado", ex.getMessage(), req);
    }

    // ── 403: acceso denegado (roles/permisos) ───────────────────────────────

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarAccesoDenegado(AccessDeniedException ex,
                                                                    HttpServletRequest req) {
        return construirRespuesta(HttpStatus.FORBIDDEN, "Acceso denegado",
                "No tienes permisos para realizar esta acción", req);
    }

    // ── 409: recurso duplicado ──────────────────────────────────────────────

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarDuplicado(RecursoDuplicadoException ex,
                                                               HttpServletRequest req) {
        return construirRespuesta(HttpStatus.CONFLICT, "Recurso duplicado", ex.getMessage(), req);
    }

    // ── 422: regla de negocio violada ───────────────────────────────────────

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarReglaNegocio(ReglaNegocioException ex,
                                                                  HttpServletRequest req) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, "Regla de negocio violada",
                ex.getMessage(), req);
    }

    /** Compatibilidad con servicios existentes (ej. CategoriaServicio.eliminar). */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarIllegalState(IllegalStateException ex,
                                                                  HttpServletRequest req) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, "Regla de negocio violada",
                ex.getMessage(), req);
    }

    // ── 400: Bean Validation en @RequestBody (@Valid) ───────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidacionRespuestaDto> manejarValidacion(MethodArgumentNotValidException ex,
                                                                          HttpServletRequest req) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errores.put(fe.getField(), fe.getDefaultMessage());
        }
        ErrorValidacionRespuestaDto body = new ErrorValidacionRespuestaDto(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Uno o más campos no son válidos",
                req.getRequestURI(),
                errores);
        return ResponseEntity.badRequest().body(body);
    }

    /** Validación de @RequestParam / @PathVariable con @Validated a nivel de clase. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorValidacionRespuestaDto> manejarConstraintViolation(ConstraintViolationException ex,
                                                                                   HttpServletRequest req) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v ->
                errores.put(v.getPropertyPath().toString(), v.getMessage()));
        ErrorValidacionRespuestaDto body = new ErrorValidacionRespuestaDto(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Uno o más parámetros no son válidos",
                req.getRequestURI(),
                errores);
        return ResponseEntity.badRequest().body(body);
    }

    /** JSON malformado o de tipo incorrecto en el body de la petición. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarJsonMalformado(HttpMessageNotReadableException ex,
                                                                    HttpServletRequest req) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Solicitud mal formada",
                "El cuerpo de la solicitud no es un JSON válido", req);
    }

    /** Tipo incorrecto en un parámetro de la URL, ej. /api/productos/abc en vez de /api/productos/1 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarTipoInvalido(MethodArgumentTypeMismatchException ex,
                                                                  HttpServletRequest req) {
        String mensaje = "El parámetro '" + ex.getName() + "' tiene un formato inválido";
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Parámetro inválido", mensaje, req);
    }

    /** Compatibilidad con servicios existentes que lanzan IllegalArgumentException para datos inválidos. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuestaDto> manejarIllegalArgument(IllegalArgumentException ex,
                                                                     HttpServletRequest req) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage(), req);
    }

    // ── 500: cualquier otro error no controlado ─────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuestaDto> manejarError(Exception ex, HttpServletRequest req) {
        // El stacktrace completo se registra solo en el log del servidor, nunca en la respuesta.
        log.error("Error no controlado en {} {}", req.getMethod(), req.getRequestURI(), ex);
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor",
                "Ocurrió un error inesperado. Intenta de nuevo más tarde.", req);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private ResponseEntity<ErrorRespuestaDto> construirRespuesta(HttpStatus status, String error,
                                                                  String mensaje, HttpServletRequest req) {
        ErrorRespuestaDto body = new ErrorRespuestaDto(status.value(), error, mensaje, req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
