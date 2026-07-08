package controlador.rest;

import com.example.PRIMESHOP.servicio.ProductoServicio;
import dto.ProductoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    private final ProductoServicio productoServicio;

    public ProductoRestController(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listarCatálogo() {
        return ResponseEntity.ok(productoServicio.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> detalleProducto(@PathVariable int id) {
        return ResponseEntity.ok(productoServicio.obtenerDetalle(id));
    }
}