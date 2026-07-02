package com.example.PRIMESHOP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para actualizar el stock de una talla específica desde el panel de inventario.
 */
public class InventarioItemDto {

    @NotNull(message = "El ID del producto-talla es obligatorio")
    private Integer productoTallaId;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer nuevoStock;

    public Integer getProductoTallaId() { return productoTallaId; }
    public void setProductoTallaId(Integer productoTallaId) { this.productoTallaId = productoTallaId; }

    public Integer getNuevoStock() { return nuevoStock; }
    public void setNuevoStock(Integer nuevoStock) { this.nuevoStock = nuevoStock; }
}
