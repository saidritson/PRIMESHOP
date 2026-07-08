package com.example.PRIMESHOP.dto;

import java.util.List;

/**
 * Representacion completa del carrito del usuario autenticado.
 */
public class CarritoDto {

    private int carritoId;
    private List<ItemCarritoDto> items;
    private int totalArticulos;
    private double total;

    public int getCarritoId() { return carritoId; }
    public void setCarritoId(int carritoId) { this.carritoId = carritoId; }

    public List<ItemCarritoDto> getItems() { return items; }
    public void setItems(List<ItemCarritoDto> items) { this.items = items; }

    public int getTotalArticulos() { return totalArticulos; }
    public void setTotalArticulos(int totalArticulos) { this.totalArticulos = totalArticulos; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
