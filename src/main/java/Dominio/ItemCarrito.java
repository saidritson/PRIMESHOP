package Dominio;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Linea de un {@link Carrito}: una talla/producto especifico y la cantidad
 * deseada. La combinacion (carrito, productoTalla) es unica: si el usuario
 * vuelve a agregar la misma talla, se incrementa la cantidad del item
 * existente en lugar de crear uno nuevo.
 */
@Entity
@Table(name = "item_carrito",
        uniqueConstraints = @UniqueConstraint(columnNames = { "carrito_id", "producto_talla_id" }))
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_talla_id", nullable = false)
    private ProductoTalla productoTalla;

    public ItemCarrito() {
    }

    public ItemCarrito(Carrito carrito, ProductoTalla productoTalla, int cantidad) {
        this.carrito = carrito;
        this.productoTalla = productoTalla;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public ProductoTalla getProductoTalla() {
        return productoTalla;
    }

    public void setProductoTalla(ProductoTalla productoTalla) {
        this.productoTalla = productoTalla;
    }

    /** @return precio unitario vigente del producto al que pertenece la talla. */
    public double getPrecioUnitario() {
        return productoTalla.getProducto().getPrecio();
    }

    /** @return importe de la linea (precio unitario x cantidad). */
    public double getSubtotal() {
        return getPrecioUnitario() * cantidad;
    }
}
