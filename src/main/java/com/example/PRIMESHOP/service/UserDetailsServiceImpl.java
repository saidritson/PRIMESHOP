package com.example.PRIMESHOP.service;

import Dominio.Usuario;
import com.example.PRIMESHOP.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("No existe un usuario con el correo: " + correo);
        }

        Usuario usuario = usuarioOpt.get();

        List<GrantedAuthority> autoridades = new ArrayList<GrantedAuthority>();
        autoridades.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));

        return new User(usuario.getCorreo(), usuario.getContrasena(), autoridades);
    }
}
