package com.example.PRIMESHOP.repository;

import Dominio.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);
}
