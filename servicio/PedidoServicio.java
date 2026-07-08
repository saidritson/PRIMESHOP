package com.example.PRIMESHOP.servicio;

import Dominio.DetallePedido;
import Dominio.Pedido;
import Dominio.ProductoTalla;
import Dominio.Usuario;
import Dominio.enums.EstadoPedido;
import com.example.PRIMESHOP.repositorio.PedidoRepositorio;
import com.example.PRIMESHOP.repository.DetallePedidoRepository;
import com.example.PRIMESHOP.repository.ProductoTallaRepository;
import com.example.PRIMESHOP.repository.UsuarioRepository;
import dto.PedidoRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Servicio de negocio para la gestión de pedidos en el panel de administración
 * y la lógica de Checkout del cliente (Storefront).
 */
@Service
@Transactional
public class PedidoServicio {

    // ── DEPENDENCIAS (Se agregaron las que ocupa tu parte del código) ──────
    private final PedidoRepositorio pedidoRepo;
    private final UsuarioRepository usuarioRepository;
    private final ProductoTallaRepository productoTallaRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoServicio(PedidoRepositorio pedidoRepo,
                          UsuarioRepository usuarioRepository,
                          ProductoTallaRepository productoTallaRepository,
                          DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepo = pedidoRepo;
        this.usuarioRepository = usuarioRepository;
        this.productoTallaRepository = productoTallaRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    // ── Consultas (Parte del Administrador) ────────────────────────────────

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepo.findAllConDetalles();
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepo.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(int id) {
        return pedidoRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorUsuario(int usuarioId) {
        return pedidoRepo.findByUsuarioIdOrdenados(usuarioId);
    }

    // ── Cambio de estado (Parte del Administrador) ─────────────────────────

    public Pedido actualizarEstado(int id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        validarTransicionEstado(pedido.getEstado(), nuevoEstado);
        pedido.setEstado(nuevoEstado);
        return pedidoRepo.save(pedido);
    }

    // ── Estadísticas para el Dashboard (Parte del Administrador) ───────────

    @Transactional(readOnly = true)
    public long contarPorEstado(EstadoPedido estado) {
        return pedidoRepo.findByEstado(estado).size();
    }

    @Transactional(readOnly = true)
    public double calcularTotalVentas() {
        return pedidoRepo.findByEstado(EstadoPedido.ENTREGADO)
                .stream()
                .mapToDouble(Pedido::getTotal)
                .sum();
    }

    private void validarTransicionEstado(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
        if (estadoActual == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede cambiar el estado de un pedido cancelado.");
        }
        if (estadoActual == EstadoPedido.ENTREGADO && nuevoEstado != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede revertir un pedido ya entregado.");
        }
    }
    @Transactional
    public void procesarCheckout(PedidoRequestDto dto, String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(new Date());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        pedido = pedidoRepo.save(pedido);

        double totalPedido = 0;

        for (PedidoRequestDto.DetallePedidoRequestDto item : dto.getItems()) {

            ProductoTalla talla = productoTallaRepository.findById(item.getIdProductoTalla())
                    .orElseThrow(() -> new RuntimeException("Talla de producto no encontrada"));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProductoTalla(talla);
            detalle.setCantidad(item.getCantidad());

            double precioUnidad = talla.getProducto().getPrecio();
            detalle.setPrecioUnitario(precioUnidad);
            totalPedido += (precioUnidad * item.getCantidad());

            detallePedidoRepository.save(detalle);
        }

        pedido.setTotal(totalPedido);
        pedidoRepo.save(pedido);
    }
}