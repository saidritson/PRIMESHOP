package com.example.PRIMESHOP.repository;

import Dominio.ProductoTalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoTallaRepository extends JpaRepository<ProductoTalla, Integer> {
}
