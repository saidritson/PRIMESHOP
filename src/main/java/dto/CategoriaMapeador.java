package com.example.PRIMESHOP.dto;

import Dominio.Categoria;
import com.example.PRIMESHOP.dto.base.MapeadorDto;
import org.springframework.stereotype.Component;

/**
 * Único punto de conversión entre la entidad JPA {@code Dominio.Categoria}
 * y sus DTOs. Ningún controlador debería construir un {@link CategoriaRespuestaDto}
 * a mano: siempre pasa por aquí.
 */
@Component
public class CategoriaMapeador implements MapeadorDto<Categoria, CategoriaFormDto, CategoriaRespuestaDto> {

    @Override
    public CategoriaRespuestaDto aDtoSalida(Categoria entidad) {
        return new CategoriaRespuestaDto(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.isActiva(),
                entidad.getCantidadProductos()
        );
    }

    @Override
    public Categoria aEntidad(CategoriaFormDto dto) {
        Categoria categoria = new Categoria(dto.getNombre(), dto.getDescripcion());
        categoria.setActiva(dto.isActiva());
        return categoria;
    }
}
