package Dominio;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private double precio;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(length = 50)
    private String color;

    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo;
    
    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoTalla> tallas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")
    private List<Resena> resenas;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, double precio, String imagenUrl,
            String color, int stockMinimo, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.color = color;
        this.stockMinimo = stockMinimo;
        this.categoria = categoria;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<ProductoTalla> getTallas() {
        return tallas;
    }

    public void setTallas(List<ProductoTalla> tallas) {
        this.tallas = tallas;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Resena> getResenas() {
        return resenas;
    }

    public void setResenas(List<Resena> resenas) {
        this.resenas = resenas;
    }

    /**
     * Calcula el stock total sumando el stock de todas las tallas.
     * @return stock total del producto
     */
    public int getStockTotal() {
        if (tallas == null || tallas.isEmpty()) {
            return 0;
        }
        return tallas.stream().mapToInt(ProductoTalla::getStock).sum();
    }

    /** @return true si no queda stock (agotado). */
    public boolean isAgotado() {
        return getStockTotal() == 0;
    }

    /** @return true si el stock cayo al umbral minimo pero aun queda (stock bajo). */
    public boolean isStockBajo() {
        int total = getStockTotal();
        return total > 0 && total <= stockMinimo;
    }
}
