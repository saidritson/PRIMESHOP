package com.example.PRIMESHOP.repository;

import Dominio.ProductoTalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoTallaRepository extends JpaRepository<ProductoTalla, Integer> {
    List<ProductoTalla> findByProductoId(Integer productoId);
}