package com.example.PRIMESHOP.servicio;

import Dominio.Categoria;
import Dominio.Producto;
import Dominio.ProductoTalla;
import com.example.PRIMESHOP.dto.ProductoFormDto;
import com.example.PRIMESHOP.repositorio.CategoriaRepositorio;
import com.example.PRIMESHOP.repositorio.ProductoRepositorio;
import com.example.PRIMESHOP.repositorio.ProductoTallaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de negocio para la gestión de productos.
 * Encapsula toda la lógica de creación, edición, listado y eliminación lógica.
 */
@Service
@Transactional
public class ProductoServicio {

    private final ProductoRepositorio productoRepo;
    private final CategoriaRepositorio categoriaRepo;
    private final ProductoTallaRepositorio tallaRepo;

    public ProductoServicio(ProductoRepositorio productoRepo,
                            CategoriaRepositorio categoriaRepo,
                            ProductoTallaRepositorio tallaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
        this.tallaRepo = tallaRepo;
    }

    // ── Consultas ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepo.findAllConTallasYCategoria();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepo.findAllActivosConTallas();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(int id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(int categoriaId) {
        return productoRepo.findByCategoriaId(categoriaId);
    }

    // ── Creación ───────────────────────────────────────────────────────────

    public Producto crear(ProductoFormDto dto) {
        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada"));

        Producto producto = new Producto(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getImagenUrl(),
                dto.getColor(),
                dto.getStockMinimo(),
                categoria
        );
        producto.setActivo(dto.isActivo());

        Producto guardado = productoRepo.save(producto);
        guardarTallas(guardado, dto);
        return guardado;
    }

    // ── Edición ────────────────────────────────────────────────────────────

    public Producto actualizar(int id, ProductoFormDto dto) {
        Producto producto = buscarPorId(id);

        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada"));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setColor(dto.getColor());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setCategoria(categoria);
        producto.setActivo(dto.isActivo());

        // Reemplazar tallas: eliminar las antiguas y recrear
        tallaRepo.deleteAll(tallaRepo.findByProductoId(id));
        guardarTallas(producto, dto);

        return productoRepo.save(producto);
    }

    // ── Eliminación lógica ─────────────────────────────────────────────────

    public void desactivar(int id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(false);
        productoRepo.save(producto);
    }

    public void activar(int id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(true);
        productoRepo.save(producto);
    }

    // ── Utilidades privadas ────────────────────────────────────────────────

    private void guardarTallas(Producto producto, ProductoFormDto dto) {
        if (dto.getTallas() == null || dto.getTallas().isEmpty()) return;

        for (int i = 0; i < dto.getTallas().size(); i++) {
            String talla = dto.getTallas().get(i);
            int stock = (dto.getStocksPorTalla() != null && i < dto.getStocksPorTalla().size())
                    ? dto.getStocksPorTalla().get(i)
                    : 0;
            tallaRepo.save(new ProductoTalla(talla, stock, producto));
        }
    }

    /** Convierte un Producto a su DTO de formulario (para precargar el form de edición). */
    @Transactional(readOnly = true)
    public ProductoFormDto toFormDto(int id) {
        Producto p = buscarPorId(id);
        ProductoFormDto dto = new ProductoFormDto();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setColor(p.getColor());
        dto.setStockMinimo(p.getStockMinimo());
        dto.setCategoriaId(p.getCategoria().getId());
        dto.setActivo(p.isActivo());

        if (p.getTallas() != null) {
            dto.setTallas(p.getTallas().stream().map(ProductoTalla::getTalla).toList());
            dto.setStocksPorTalla(p.getTallas().stream().map(ProductoTalla::getStock).toList());
        }
        return dto;
    }
}
