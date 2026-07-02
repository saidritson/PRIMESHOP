package com.example.PRIMESHOP.repositorio;

import Dominio.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoriaId(int categoriaId);

    List<Producto> findByActivo(boolean activo);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT p FROM Producto p JOIN FETCH p.tallas WHERE p.activo = true")
    List<Producto> findAllActivosConTallas();

    @Query("SELECT p FROM Producto p JOIN FETCH p.tallas JOIN FETCH p.categoria")
    List<Producto> findAllConTallasYCategoria();
}
