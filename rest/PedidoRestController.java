package controlador.rest;

import com.example.PRIMESHOP.servicio.PedidoServicio;
import dto.PedidoRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    private final PedidoServicio pedidoServicio;

    public PedidoRestController(PedidoServicio pedidoServicio) {
        this.pedidoServicio = pedidoServicio;
    }

    @PostMapping
    public ResponseEntity<String> procesarCompra(@Valid @RequestBody PedidoRequestDto request, Principal principal) {
        String correoAutenticado = (principal != null) ? principal.getName() : "test@usuario.com";
        pedidoServicio.procesarCheckout(request, correoAutenticado);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pedido creado exitosamente");
    }
}