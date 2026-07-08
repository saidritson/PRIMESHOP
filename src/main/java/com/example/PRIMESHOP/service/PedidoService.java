package com.example.PRIMESHOP.service;

import Dominio.Pedido;
import Dominio.enums.EstadoPedido;
import com.example.PRIMESHOP.repository.PedidoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio para la administracion de ordenes (pedidos).
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAllByOrderByFechaDesc();
    }

    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByFechaDesc(estado);
    }

    public Pedido buscar(int id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public long contarPendientes() {
        return pedidoRepository.countByEstado(EstadoPedido.PENDIENTE);
    }

    public List<Pedido> ultimas(int cantidad) {
        return pedidoRepository.findAllByOrderByFechaDesc().stream().limit(cantidad).toList();
    }

    public double ventasDelDia() {
        LocalDate hoy = LocalDate.now();
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getFecha() != null && aLocalDate(p.getFecha()).equals(hoy))
                .mapToDouble(Pedido::getTotal)
                .sum();
    }

    public void cambiarEstado(int id, EstadoPedido estado) {
        pedidoRepository.findById(id).ifPresent(p -> {
            p.setEstado(estado);
            pedidoRepository.save(p);
        });
    }

    private LocalDate aLocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
