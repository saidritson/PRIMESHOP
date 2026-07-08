package com.example.PRIMESHOP.service;

import Dominio.Carrito;
import Dominio.DetallePedido;
import Dominio.Direccion;
import Dominio.ItemCarrito;
import Dominio.Pago;
import Dominio.Pedido;
import Dominio.ProductoTalla;
import Dominio.Usuario;
import Dominio.enums.EstadoPago;
import Dominio.enums.EstadoPedido;
import Dominio.enums.MetodoPago;
import com.example.PRIMESHOP.dto.CheckoutRequestDto;
import com.example.PRIMESHOP.dto.DetallePedidoResponseDto;
import com.example.PRIMESHOP.dto.PedidoResponseDto;
import com.example.PRIMESHOP.repository.DetallePedidoRepository;
import com.example.PRIMESHOP.repository.DireccionRepository;
import com.example.PRIMESHOP.repository.ItemCarritoRepository;
import com.example.PRIMESHOP.repository.PedidoRepository;
import com.example.PRIMESHOP.repository.ProductoTallaRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Procesa el "Checkout": toma el contenido actual del carrito del usuario,
 * valida existencias y direccion, genera el {@link Pedido} definitivo con
 * sus {@link DetallePedido} y su {@link Pago}, descuenta el stock reservado
 * y finalmente vacia el carrito.
 *
 * Todo ocurre dentro de una sola transaccion: si algo falla (p. ej. no hay
 * stock suficiente de un articulo) no se guarda nada a medias.
 */
@Service
@Transactional
public class CheckoutService {

    private final CarritoService carritoService;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoTallaRepository productoTallaRepository;
    private final DireccionRepository direccionRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public CheckoutService(CarritoService carritoService,
                           ItemCarritoRepository itemCarritoRepository,
                           ProductoTallaRepository productoTallaRepository,
                           DireccionRepository direccionRepository,
                           PedidoRepository pedidoRepository,
                           DetallePedidoRepository detallePedidoRepository) {
        this.carritoService = carritoService;
        this.itemCarritoRepository = itemCarritoRepository;
        this.productoTallaRepository = productoTallaRepository;
        this.direccionRepository = direccionRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    /** Historial de ordenes del usuario autenticado, mas recientes primero. */
    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarMisPedidos(Usuario usuario) {
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId()).stream()
                .map(this::aResponseDto)
                .toList();
    }

    public PedidoResponseDto procesarCheckout(Usuario usuario, CheckoutRequestDto request) {
        Carrito carrito = carritoService.obtenerOCrear(usuario);
        List<ItemCarrito> items = itemCarritoRepository.findByCarritoIdOrderByIdAsc(carrito.getId());

        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito esta vacio");
        }

        Direccion direccion = direccionRepository.findById(request.getDireccionId())
                .orElseThrow(() -> new NoSuchElementException("Direccion no encontrada"));
        if (direccion.getUsuario().getId() != usuario.getId()) {
            throw new AccessDeniedException("La direccion seleccionada no pertenece al usuario");
        }

        // Revalidar existencias por si cambiaron desde que se agregaron al carrito.
        for (ItemCarrito item : items) {
            ProductoTalla talla = item.getProductoTalla();
            if (item.getCantidad() > talla.getStock()) {
                throw new IllegalStateException(
                        "Stock insuficiente para " + talla.getProducto().getNombre()
                                + " (talla " + talla.getTalla() + "). Disponible: " + talla.getStock());
            }
        }

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(generarNumeroPedido());
        pedido.setFecha(new Date());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(direccion);

        double total = 0;
        List<DetallePedido> detalles = new ArrayList<>();
        for (ItemCarrito item : items) {
            ProductoTalla talla = item.getProductoTalla();
            double precioUnitario = talla.getProducto().getPrecio();

            DetallePedido detalle = new DetallePedido(item.getCantidad(), precioUnitario,
                    talla.getProducto(), talla, pedido);
            detalles.add(detalle);
            total += detalle.getSubtotal();

            // Se descuenta el stock reservado por el pedido.
            talla.setStock(talla.getStock() - item.getCantidad());
            productoTallaRepository.save(talla);
        }
        pedido.setDetalles(detalles);
        pedido.setTotal(total);

        Pago pago = new Pago(total, new Date(), request.getMetodoPago(), estadoInicialPago(request.getMetodoPago()));
        pedido.setPago(pago);

        // Un solo save: el cascade ALL de Pedido inserta Pago y cada
        // DetallePedido en la misma operacion, en el orden correcto
        // (Pedido -> Pago -> Detalles). Guardar el Pedido mas de una vez
        // con la coleccion "detalles" reasignada a mano dispara el
        // orphanRemoval y borra lo que se acaba de insertar.
        pedido = pedidoRepository.save(pedido);

        carritoService.vaciarCarrito(usuario);

        return aResponseDto(pedido);
    }

    /**
     * El pago contra entrega queda pendiente hasta que el repartidor lo
     * cobre; los demas metodos se dan por aprobados de inmediato ya que
     * este proyecto no integra una pasarela de pago real.
     */
    private EstadoPago estadoInicialPago(MetodoPago metodo) {
        return (metodo == MetodoPago.CONTRA_ENTREGA) ? EstadoPago.PENDIENTE : EstadoPago.APROBADO;
    }

    private String generarNumeroPedido() {
        return "PS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PedidoResponseDto aResponseDto(Pedido pedido) {
        PedidoResponseDto dto = new PedidoResponseDto();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setFecha(pedido.getFecha());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());
        if (pedido.getPago() != null) {
            dto.setMetodoPago(pedido.getPago().getMetodo());
            dto.setEstadoPago(pedido.getPago().getEstado());
        }
        dto.setDetalles(pedido.getDetalles().stream().map(this::aDetalleDto).toList());
        return dto;
    }

    private DetallePedidoResponseDto aDetalleDto(DetallePedido detalle) {
        DetallePedidoResponseDto dto = new DetallePedidoResponseDto();
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setTalla(detalle.getProductoTalla() != null ? detalle.getProductoTalla().getTalla() : null);
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
