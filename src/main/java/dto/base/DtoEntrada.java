package com.example.PRIMESHOP.dto.base;

/**
 * Marca a un DTO como un objeto de <b>entrada</b>: representa datos que llegan
 * desde el cliente (body de un POST/PUT, formulario, etc.) y que serán
 * validados con Bean Validation antes de tocar la capa de servicio.
 * <p>
 * Un {@code DtoEntrada} nunca debe ser devuelto como respuesta ni contener
 * campos que solo existan en la entidad JPA (ids autogenerados, relaciones
 * completas, campos de auditoría, etc.).
 */
public interface DtoEntrada {
}
