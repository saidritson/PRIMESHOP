package com.example.PRIMESHOP.dto.base;

/**
 * Contrato estándar para convertir entre una entidad JPA y sus DTOs de
 * entrada/salida. Implementarlo (normalmente como métodos estáticos o un
 * bean {@code @Component}) deja explícito, en un solo lugar por entidad,
 * el punto exacto donde el dominio "cruza" hacia la capa web.
 *
 * @param <E>   la entidad de dominio (paquete {@code Dominio})
 * @param <REQ> el DTO de entrada usado para crear/actualizar la entidad
 * @param <RES> el DTO de salida devuelto al cliente
 */
public interface MapeadorDto<E, REQ extends DtoEntrada, RES extends DtoSalida> {

    /** Convierte la entidad de dominio en el DTO que se expone al cliente. */
    RES aDtoSalida(E entidad);

    /** Crea una entidad nueva a partir de un DTO de entrada ya validado. */
    E aEntidad(REQ dtoEntrada);
}
