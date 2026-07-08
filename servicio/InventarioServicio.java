package com.example.PRIMESHOP.servicio;

import Dominio.Producto;
import Dominio.ProductoTalla;
import com.example.PRIMESHOP.repositorio.ProductoRepositorio;
import com.example.PRIMESHOP.repositorio.ProductoTallaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de negocio para la gestión del inventario (stock por talla).
 */
@Service
@Transactional
public class InventarioServicio {

    private final ProductoRepositorio productoRepo;
    private final ProductoTallaRepositorio tallaRepo;

    public InventarioServicio(ProductoRepositorio productoRepo,
                              ProductoTallaRepositorio tallaRepo) {
        this.productoRepo = productoRepo;
        this.tallaRepo = tallaRepo;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarProductosConStock() {
        return productoRepo.findAllConTallasYCategoria();
    }

    /**
     * Actualiza el stock de una talla específica.
     *
     * @param productoTallaId ID del registro ProductoTalla
     * @param nuevoStock      cantidad nueva (>= 0)
     */
    public ProductoTalla actualizarStock(int productoTallaId, int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        ProductoTalla talla = tallaRepo.findById(productoTallaId)
                .orElseThrow(() -> new NoSuchElementException("Registro de talla no encontrado: " + productoTallaId));
        talla.setStock(nuevoStock);
        return tallaRepo.save(talla);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarProductosConStockBajo() {
        return productoRepo.findByActivo(true).stream()
                .filter(Producto::isStockBajo)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarProductosAgotados() {
        return productoRepo.findByActivo(true).stream()
                .filter(Producto::isAgotado)
                .toList();
    }
}
