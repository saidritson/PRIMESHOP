package com.example.PRIMESHOP.dto;

import jakarta.validation.constraints.*;

import java.util.List;

/**
 * DTO para el formulario de creación y edición de productos.
 * Contiene todas las validaciones de Bean Validation.
 */
public class ProductoFormDto {

    private Integer id; // null = nuevo, non-null = edición

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "999999.99", message = "El precio no puede superar $999,999.99")
    private Double precio;

    @Size(max = 255, message = "La URL de imagen no puede superar los 255 caracteres")
    private String imagenUrl;

    @Size(max = 50, message = "El color no puede superar los 50 caracteres")
    private String color;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @NotNull(message = "Debe seleccionar una categoría")
    @Positive(message = "Debe seleccionar una categoría válida")
    private Integer categoriaId;

    private boolean activo = true;

    /** Lista de tallas enviadas desde el formulario (ej. ["S","M","L"]). */
    private List<String> tallas;

    /** Stock inicial por talla, paralelo al listado {@code tallas}. */
    private List<Integer> stocksPorTalla;

    // ── Getters y Setters ──────────────────────────────────────────────────

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<String> getTallas() { return tallas; }
    public void setTallas(List<String> tallas) { this.tallas = tallas; }

    public List<Integer> getStocksPorTalla() { return stocksPorTalla; }
    public void setStocksPorTalla(List<Integer> stocksPorTalla) { this.stocksPorTalla = stocksPorTalla; }
}
