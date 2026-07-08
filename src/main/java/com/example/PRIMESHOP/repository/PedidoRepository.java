package com.example.PRIMESHOP.repository;

import Dominio.Pedido;
import Dominio.enums.EstadoPedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findAllByOrderByFechaDesc();

    List<Pedido> findByEstadoOrderByFechaDesc(EstadoPedido estado);

    List<Pedido> findByUsuarioIdOrderByFechaDesc(int usuarioId);

    long countByEstado(EstadoPedido estado);
}
