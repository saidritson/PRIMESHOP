package com.example.PRIMESHOP.repositorio;

import Dominio.ProductoTalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoTallaRepositorio extends JpaRepository<ProductoTalla, Integer> {

    List<ProductoTalla> findByProductoId(int productoId);
}
