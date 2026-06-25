package com.example.PRIMESHOP.servicio;

import Dominio.Pedido;
import Dominio.enums.EstadoPedido;
import com.example.PRIMESHOP.repositorio.PedidoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de negocio para la gestión de pedidos en el panel de administración.
 */
@Service
@Transactional
public class PedidoServicio {

    private final PedidoRepositorio pedidoRepo;

    public PedidoServicio(PedidoRepositorio pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    // ── Consultas ──────────────────────────────────────────────────────────

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

    // ── Cambio de estado ───────────────────────────────────────────────────

    public Pedido actualizarEstado(int id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        validarTransicionEstado(pedido.getEstado(), nuevoEstado);
        pedido.setEstado(nuevoEstado);
        return pedidoRepo.save(pedido);
    }

    // ── Estadísticas para el Dashboard ────────────────────────────────────

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

    // ── Privados ───────────────────────────────────────────────────────────

    /**
     * Valida que la transición de estados sea coherente.
     * Un pedido cancelado o entregado no puede cambiar de estado.
     */
    private void validarTransicionEstado(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
        if (estadoActual == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede cambiar el estado de un pedido cancelado.");
        }
        if (estadoActual == EstadoPedido.ENTREGADO && nuevoEstado != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede revertir un pedido ya entregado.");
        }
    }
}
