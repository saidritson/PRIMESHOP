package com.example.PRIMESHOP.repository;

import Dominio.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoriaId(Integer categoriaId);
    @Query("SELECT p FROM Producto p WHERE LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Producto> buscarPorDescripcion(@Param("filtro") String filtro);
}
