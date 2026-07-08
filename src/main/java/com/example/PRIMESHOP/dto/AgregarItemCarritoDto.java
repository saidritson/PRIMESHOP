package com.example.PRIMESHOP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo de la peticion para agregar un articulo (talla de un producto) al carrito.
 * Si la talla ya esta en el carrito, la cantidad se suma a la existente.
 */
public class AgregarItemCarritoDto {

    @NotNull(message = "Debe indicar la talla del producto")
    private Integer productoTallaId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    public Integer getProductoTallaId() { return productoTallaId; }
    public void setProductoTallaId(Integer productoTallaId) { this.productoTallaId = productoTallaId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
