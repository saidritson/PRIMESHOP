package com.example.PRIMESHOP.config;

import Dominio.Usuario;
import Dominio.enums.RolUsuario;
import com.example.PRIMESHOP.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Usuario> existente = usuarioRepository.findByCorreo("admin@primeshop.com");

        if (existente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setCorreo("admin@primeshop.com");
            admin.setContrasena(passwordEncoder.encode("1234"));
            admin.setTelefono("0000000000");
            admin.setRol(RolUsuario.ADMINISTRADOR);

            usuarioRepository.save(admin);
        }
    }
}
