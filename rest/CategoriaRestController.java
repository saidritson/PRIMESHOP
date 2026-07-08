package controlador.rest;

import com.example.PRIMESHOP.servicio.CategoriaServicio;
import dto.CategoriaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    private final CategoriaServicio categoriaServicio;

    public CategoriaRestController(CategoriaServicio categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listarCategorias() {
        return ResponseEntity.ok(categoriaServicio.listarCategorias());
    }
}