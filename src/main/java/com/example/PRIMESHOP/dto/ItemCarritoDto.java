package com.example.PRIMESHOP.dto;

/**
 * Representa una linea del carrito hacia el cliente (front-end).
 */
public class ItemCarritoDto {

    private int itemId;
    private int productoId;
    private String nombreProducto;
    private String imagenUrl;
    private int productoTallaId;
    private String talla;
    private double precioUnitario;
    private int cantidad;
    private double subtotal;
    private int stockDisponible;

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getProductoTallaId() { return productoTallaId; }
    public void setProductoTallaId(int productoTallaId) { this.productoTallaId = productoTallaId; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }
}
