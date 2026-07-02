package com.example.PRIMESHOP.dto;

import com.example.PRIMESHOP.dto.base.DtoSalida;

/**
 * DTO de salida para categorías: lo que la API expone al cliente.
 * A diferencia de la entidad {@code Dominio.Categoria}, no incluye la
 * colección {@code productos} (evita relaciones lazy y payloads enormes),
 * solo el conteo ya calculado por la entidad.
 */
public class CategoriaRespuestaDto implements DtoSalida {

    private int id;
    private String nombre;
    private String descripcion;
    private boolean activa;
    private int cantidadProductos;

    public CategoriaRespuestaDto() {
    }

    public CategoriaRespuestaDto(int id, String nombre, String descripcion, boolean activa,
                                  int cantidadProductos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activa = activa;
        this.cantidadProductos = cantidadProductos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }
}
