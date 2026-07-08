package com.example.PRIMESHOP.service;

import Dominio.Categoria;
import Dominio.Producto;
import Dominio.ProductoTalla;
import com.example.PRIMESHOP.form.ProductoForm;
import com.example.PRIMESHOP.repository.CategoriaRepository;
import com.example.PRIMESHOP.repository.ProductoRepository;
import com.example.PRIMESHOP.repository.ProductoTallaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio para la administracion de productos y su inventario.
 * El stock se maneja por talla; al crear un producto se genera una talla
 * por defecto ("Unica") para poder ajustar existencias desde inventario.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoTallaRepository tallaRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
            ProductoTallaRepository tallaRepository,
            CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.tallaRepository = tallaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto buscar(int id) {
        return productoRepository.findById(id).orElse(null);
    }

    public long contarActivos() {
        return productoRepository.countByActivoTrue();
    }

    /** Productos con stock bajo o agotados (para alertas e inventario). */
    public List<Producto> stockBajo() {
        return productoRepository.findAll().stream()
                .filter(p -> p.isStockBajo() || p.isAgotado())
                .toList();
    }

    @Transactional
    public void guardar(ProductoForm form) {
        boolean nuevo = (form.getId() == null);
        Producto producto = nuevo
                ? new Producto()
                : productoRepository.findById(form.getId()).orElseThrow();

        producto.setNombre(form.getNombre());
        producto.setDescripcion(form.getDescripcion());
        producto.setPrecio(form.getPrecio());
        producto.setColor(form.getColor());
        producto.setStockMinimo(form.getStockMinimo());
        producto.setActivo(form.isActivo());

        Categoria categoria = categoriaRepository.findById(form.getCategoriaId()).orElseThrow();
        producto.setCategoria(categoria);

        productoRepository.save(producto);

        if (nuevo) {
            ProductoTalla talla = new ProductoTalla("Unica", form.getStockInicial(), producto);
            tallaRepository.save(talla);
        }
    }

    public void cambiarEstado(int id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(!p.isActivo());
            productoRepository.save(p);
        });
    }

    @Transactional
    public void eliminar(int id) {
        productoRepository.deleteById(id);
    }

    /** Suma (o resta) existencias a la primera talla del producto, sin bajar de 0. */
    @Transactional
    public void ajustarStock(int productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        List<ProductoTalla> tallas = producto.getTallas();
        ProductoTalla talla = (tallas == null || tallas.isEmpty())
                ? new ProductoTalla("Unica", 0, producto)
                : tallas.get(0);

        int nuevo = talla.getStock() + cantidad;
        talla.setStock(Math.max(nuevo, 0));
        tallaRepository.save(talla);
    }

    /** Rellena un formulario con los datos de un producto existente (para editar). */
    public ProductoForm aFormulario(Producto p) {
        ProductoForm form = new ProductoForm();
        form.setId(p.getId());
        form.setNombre(p.getNombre());
        form.setDescripcion(p.getDescripcion());
        form.setPrecio(p.getPrecio());
        form.setColor(p.getColor());
        form.setStockMinimo(p.getStockMinimo());
        form.setStockInicial(p.getStockTotal());
        form.setActivo(p.isActivo());
        if (p.getCategoria() != null) {
            form.setCategoriaId(p.getCategoria().getId());
        }
        return form;
    }
}
