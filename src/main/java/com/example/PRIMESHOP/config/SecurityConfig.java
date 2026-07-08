package com.example.PRIMESHOP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;


/**
 * @author Jesus Rodrigo Villegas Arguelles - 261186, Said Roel Chavez Luzania - 252168
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login", "/Styles/**", "/Imagenes/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                        // Catalogo publico: cualquiera puede consultarlo, sin iniciar sesion.
                        .requestMatchers("/api/productos/**", "/api/categorias/**").permitAll()
                        // Carrito y checkout son endpoints protegidos: requieren usuario autenticado.
                        .requestMatchers("/api/carrito/**", "/api/pedidos/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .usernameParameter("correo")
                        .passwordParameter("contrasena")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                // La API (/api/**) es JSON puro, sin formularios server-side: se usa un
                // token CSRF en cookie (en vez del que auto-inyecta Thymeleaf en los
                // formularios de /admin) y, si falta la sesion, se responde 401 en JSON
                // en lugar de redirigir al login de administracion.
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/api/**"))
                )
                .exceptionHandling(handling -> handling
                        .defaultAuthenticationEntryPointFor(
                                apiAuthenticationEntryPoint(),
                                PathPatternRequestMatcher.withDefaults().matcher("/api/**"))
                );

        return http.build();
    }

    /** Para peticiones a la API sin sesion, responde 401 en vez de redirigir a /admin/login. */
    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
        return (request, response, authException) ->
                response.sendError(401, "Debe iniciar sesion para acceder a este recurso");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
