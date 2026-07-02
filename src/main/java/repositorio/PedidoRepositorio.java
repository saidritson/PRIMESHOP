package com.example.PRIMESHOP.repositorio;

import Dominio.Pedido;
import Dominio.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuarioId(int usuarioId);

    List<Pedido> findByEstado(EstadoPedido estado);

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario JOIN FETCH p.detalles ORDER BY p.fecha DESC")
    List<Pedido> findAllConDetalles();

    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario WHERE p.usuario.id = :usuarioId ORDER BY p.fecha DESC")
    List<Pedido> findByUsuarioIdOrdenados(int usuarioId);
}
