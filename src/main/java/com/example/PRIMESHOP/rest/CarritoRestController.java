package com.example.PRIMESHOP.rest;

import Dominio.Usuario;
import com.example.PRIMESHOP.dto.AgregarItemCarritoDto;
import com.example.PRIMESHOP.dto.CarritoDto;
import com.example.PRIMESHOP.service.CarritoService;
import com.example.PRIMESHOP.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints protegidos (requieren sesion iniciada) para la gestion del
 * carrito de compra del usuario autenticado: agregar, incrementar,
 * disminuir y quitar articulos.
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoRestController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;

    public CarritoRestController(CarritoService carritoService, UsuarioService usuarioService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
    }

    /** Devuelve el carrito actual del usuario autenticado. */
    @GetMapping
    public ResponseEntity<CarritoDto> verCarrito(Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(carritoService.obtenerCarritoDto(usuario));
    }

    /** Agrega una talla de producto al carrito (o suma cantidad si ya estaba). */
    @PostMapping("/items")
    public ResponseEntity<CarritoDto> agregarItem(@Valid @RequestBody AgregarItemCarritoDto request,
            Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        CarritoDto carrito = carritoService.agregarItem(usuario, request.getProductoTallaId(), request.getCantidad());
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    /** Suma 1 a la cantidad de un articulo del carrito. */
    @PutMapping("/items/{itemId}/incrementar")
    public ResponseEntity<CarritoDto> incrementar(@PathVariable int itemId, Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(carritoService.incrementarItem(usuario, itemId));
    }

    /** Resta 1 a la cantidad de un articulo del carrito (lo quita si llega a 0). */
    @PutMapping("/items/{itemId}/disminuir")
    public ResponseEntity<CarritoDto> disminuir(@PathVariable int itemId, Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(carritoService.disminuirItem(usuario, itemId));
    }

    /** Quita por completo un articulo del carrito. */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoDto> eliminarItem(@PathVariable int itemId, Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(carritoService.eliminarItem(usuario, itemId));
    }

    /** Vacia el carrito por completo. */
    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        carritoService.vaciarCarrito(usuario);
        return ResponseEntity.noContent().build();
    }
}
