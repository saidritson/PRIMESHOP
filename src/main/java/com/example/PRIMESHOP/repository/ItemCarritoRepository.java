package com.example.PRIMESHOP.repository;

import Dominio.ItemCarrito;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Integer> {

    List<ItemCarrito> findByCarritoIdOrderByIdAsc(int carritoId);

    Optional<ItemCarrito> findByCarritoIdAndProductoTallaId(int carritoId, int productoTallaId);
}
