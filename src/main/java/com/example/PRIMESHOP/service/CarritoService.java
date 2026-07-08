package com.example.PRIMESHOP.service;

import Dominio.Carrito;
import Dominio.ItemCarrito;
import Dominio.ProductoTalla;
import Dominio.Usuario;
import com.example.PRIMESHOP.dto.CarritoDto;
import com.example.PRIMESHOP.dto.ItemCarritoDto;
import com.example.PRIMESHOP.repository.CarritoRepository;
import com.example.PRIMESHOP.repository.ItemCarritoRepository;
import com.example.PRIMESHOP.repository.ProductoTallaRepository;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio del carrito de compra del usuario autenticado:
 * agregar, incrementar, disminuir y quitar articulos. El carrito vive en
 * base de datos (uno por usuario) para que sobreviva entre sesiones.
 */
@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoTallaRepository productoTallaRepository;

    public CarritoService(CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            ProductoTallaRepository productoTallaRepository) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.productoTallaRepository = productoTallaRepository;
    }

    /** Obtiene el carrito del usuario, creando uno vacio si aun no tiene. */
    public Carrito obtenerOCrear(Usuario usuario) {
        return carritoRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> carritoRepository.save(new Carrito(usuario)));
    }

    @Transactional(readOnly = true)
    public CarritoDto obtenerCarritoDto(Usuario usuario) {
        Carrito carrito = obtenerOCrear(usuario);
        return aDto(carrito);
    }

    /** Agrega una talla al carrito; si ya existe, suma la cantidad a la existente. */
    public CarritoDto agregarItem(Usuario usuario, int productoTallaId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        ProductoTalla talla = productoTallaRepository.findById(productoTallaId)
                .orElseThrow(() -> new NoSuchElementException("Talla de producto no encontrada"));

        Carrito carrito = obtenerOCrear(usuario);
        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoTallaId(carrito.getId(), productoTallaId)
                .orElse(null);

        int cantidadDeseada = (item == null) ? cantidad : item.getCantidad() + cantidad;
        validarStock(talla, cantidadDeseada);

        if (item == null) {
            item = new ItemCarrito(carrito, talla, cantidadDeseada);
        } else {
            item.setCantidad(cantidadDeseada);
        }
        itemCarritoRepository.save(item);
        tocar(carrito);

        return aDto(carrito);
    }

    /** Suma 1 a la cantidad de un item del carrito. */
    public CarritoDto incrementarItem(Usuario usuario, int itemId) {
        ItemCarrito item = obtenerItemDeUsuario(usuario, itemId);
        validarStock(item.getProductoTalla(), item.getCantidad() + 1);
        item.setCantidad(item.getCantidad() + 1);
        itemCarritoRepository.save(item);
        tocar(item.getCarrito());
        return aDto(item.getCarrito());
    }

    /** Resta 1 a la cantidad de un item del carrito; si llega a 0, se elimina. */
    public CarritoDto disminuirItem(Usuario usuario, int itemId) {
        ItemCarrito item = obtenerItemDeUsuario(usuario, itemId);
        Carrito carrito = item.getCarrito();

        if (item.getCantidad() <= 1) {
            itemCarritoRepository.delete(item);
        } else {
            item.setCantidad(item.getCantidad() - 1);
            itemCarritoRepository.save(item);
        }
        tocar(carrito);
        return aDto(carrito);
    }

    /** Quita por completo un item del carrito, sin importar su cantidad. */
    public CarritoDto eliminarItem(Usuario usuario, int itemId) {
        ItemCarrito item = obtenerItemDeUsuario(usuario, itemId);
        Carrito carrito = item.getCarrito();
        itemCarritoRepository.delete(item);
        tocar(carrito);
        return aDto(carrito);
    }

    /** Vacia el carrito por completo (se usa tambien al confirmar el checkout). */
    public void vaciarCarrito(Usuario usuario) {
        Carrito carrito = obtenerOCrear(usuario);
        List<ItemCarrito> items = itemCarritoRepository.findByCarritoIdOrderByIdAsc(carrito.getId());
        itemCarritoRepository.deleteAll(items);
        tocar(carrito);
    }

    // ── Helpers privados ─────────────────────────────────────────────────

    private ItemCarrito obtenerItemDeUsuario(Usuario usuario, int itemId) {
        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Articulo de carrito no encontrado"));

        if (item.getCarrito().getUsuario().getId() != usuario.getId()) {
            throw new AccessDeniedException("Este articulo no pertenece a tu carrito");
        }
        return item;
    }

    private void validarStock(ProductoTalla talla, int cantidadDeseada) {
        if (cantidadDeseada > talla.getStock()) {
            throw new IllegalStateException(
                    "Stock insuficiente para " + talla.getProducto().getNombre()
                            + " (talla " + talla.getTalla() + "). Disponible: " + talla.getStock());
        }
    }

    private void tocar(Carrito carrito) {
        carrito.setFechaActualizacion(new Date());
        carritoRepository.save(carrito);
    }

    private CarritoDto aDto(Carrito carrito) {
        List<ItemCarrito> items = itemCarritoRepository.findByCarritoIdOrderByIdAsc(carrito.getId());

        CarritoDto dto = new CarritoDto();
        dto.setCarritoId(carrito.getId());
        dto.setItems(items.stream().map(this::aItemDto).toList());
        dto.setTotalArticulos(items.stream().mapToInt(ItemCarrito::getCantidad).sum());
        dto.setTotal(items.stream().mapToDouble(ItemCarrito::getSubtotal).sum());
        return dto;
    }

    private ItemCarritoDto aItemDto(ItemCarrito item) {
        ProductoTalla talla = item.getProductoTalla();

        ItemCarritoDto dto = new ItemCarritoDto();
        dto.setItemId(item.getId());
        dto.setProductoId(talla.getProducto().getId());
        dto.setNombreProducto(talla.getProducto().getNombre());
        dto.setImagenUrl(talla.getProducto().getImagenUrl());
        dto.setProductoTallaId(talla.getId());
        dto.setTalla(talla.getTalla());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setCantidad(item.getCantidad());
        dto.setSubtotal(item.getSubtotal());
        dto.setStockDisponible(talla.getStock());
        return dto;
    }
}
