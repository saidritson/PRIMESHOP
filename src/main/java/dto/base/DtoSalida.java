package com.example.PRIMESHOP.dto.base;

/**
 * Marca a un DTO como un objeto de <b>salida</b>: representa exactamente lo
 * que la API expone al cliente.
 * <p>
 * Regla de arquitectura: los controladores REST (@RestController) nunca
 * devuelven entidades {@code Dominio.*} directamente. Siempre se mapea la
 * entidad a un {@code DtoSalida} (a mano o mediante un
 * {@link MapeadorDto}), evitando exponer relaciones lazy, contraseñas u
 * otros campos internos, y desacoplando el contrato de la API del modelo
 * de persistencia.
 */
public interface DtoSalida {
}
