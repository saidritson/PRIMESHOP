package com.example.PRIMESHOP.service;

import Dominio.Usuario;
import com.example.PRIMESHOP.repository.UsuarioRepository;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Resuelve la entidad {@link Usuario} correspondiente al usuario
     * autenticado en la peticion actual (el "correo" es el username usado
     * por {@link UserDetailsServiceImpl}).
     *
     * @throws AccessDeniedException si no hay una sesion autenticada
     * @throws NoSuchElementException si el usuario ya no existe en la base de datos
     */
    public Usuario obtenerUsuarioAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Debe iniciar sesion para realizar esta accion");
        }
        return usuarioRepository.findByCorreo(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    }
}
