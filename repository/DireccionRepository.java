package com.example.PRIMESHOP.repository;

import Dominio.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    List<Direccion> findByUsuarioId(Integer usuarioId);
}