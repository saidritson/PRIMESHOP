package com.example.PRIMESHOP.rest;

import Dominio.Usuario;
import com.example.PRIMESHOP.dto.CheckoutRequestDto;
import com.example.PRIMESHOP.dto.PedidoResponseDto;
import com.example.PRIMESHOP.service.CheckoutService;
import com.example.PRIMESHOP.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint protegido (requiere sesion iniciada) que procesa la compra:
 * toma el carrito del usuario autenticado y genera la orden (Pedido)
 * definitiva en la base de datos.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    private final CheckoutService checkoutService;
    private final UsuarioService usuarioService;

    public PedidoRestController(CheckoutService checkoutService, UsuarioService usuarioService) {
        this.checkoutService = checkoutService;
        this.usuarioService = usuarioService;
    }

    /** Confirma la compra: convierte el carrito actual en un pedido. */
    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponseDto> checkout(@Valid @RequestBody CheckoutRequestDto request,
            Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        PedidoResponseDto pedido = checkoutService.procesarCheckout(usuario, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    /** Historial de ordenes propias del usuario autenticado. */
    @GetMapping("/mios")
    public ResponseEntity<List<PedidoResponseDto>> misPedidos(Authentication authentication) {
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado(authentication);
        return ResponseEntity.ok(checkoutService.listarMisPedidos(usuario));
    }
}
