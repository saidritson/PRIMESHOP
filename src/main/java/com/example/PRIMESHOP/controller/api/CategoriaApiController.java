package com.example.PRIMESHOP.controller.api;

import com.example.PRIMESHOP.dto.CategoriaFormDto;
import com.example.PRIMESHOP.dto.CategoriaMapeador;
import com.example.PRIMESHOP.dto.CategoriaRespuestaDto;
import com.example.PRIMESHOP.dto.base.RespuestaApiDto;
import com.example.PRIMESHOP.servicio.CategoriaServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ejemplo de referencia de la arquitectura REST del proyecto:
 * <ul>
 *   <li>Solo recibe/devuelve {@code DtoEntrada}/{@code DtoSalida}, nunca la entidad {@code Dominio.Categoria}.</li>
 *   <li>{@code @Valid} dispara Bean Validation; los errores los captura {@code ManejadorGlobalExcepciones}.</li>
 *   <li>Cualquier {@code RecursoNoEncontradoException}/{@code RecursoDuplicadoException}/{@code ReglaNegocioException}
 *       lanzada por {@code CategoriaServicio} se traduce automáticamente a JSON estructurado con el status correcto.</li>
 * </ul>
 * Rutas base: /api/categorias
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaApiController {

    private final CategoriaServicio categoriaServicio;
    private final CategoriaMapeador mapeador;

    public CategoriaApiController(CategoriaServicio categoriaServicio, CategoriaMapeador mapeador) {
        this.categoriaServicio = categoriaServicio;
        this.mapeador = mapeador;
    }

    @GetMapping
    public RespuestaApiDto<List<CategoriaRespuestaDto>> listar() {
        List<CategoriaRespuestaDto> categorias = categoriaServicio.listarTodas().stream()
                .map(mapeador::aDtoSalida)
                .toList();
        return RespuestaApiDto.ok(categorias);
    }

    @GetMapping("/{id}")
    public RespuestaApiDto<CategoriaRespuestaDto> obtener(@PathVariable int id) {
        // Si no existe, CategoriaServicio.buscarPorId lanza RecursoNoEncontradoException → 404 automático.
        return RespuestaApiDto.ok(mapeador.aDtoSalida(categoriaServicio.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<RespuestaApiDto<CategoriaRespuestaDto>> crear(@Valid @RequestBody CategoriaFormDto dto) {
        // @Valid: si falla (@NotBlank, @Size, etc.) → 400 con detalle por campo, sin llegar aquí.
        // Nombre repetido → RecursoDuplicadoException → 409, manejado también de forma centralizada.
        var creada = categoriaServicio.crear(dto);
        var body = RespuestaApiDto.ok("Categoría creada exitosamente", mapeador.aDtoSalida(creada));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{id}")
    public RespuestaApiDto<CategoriaRespuestaDto> actualizar(@PathVariable int id,
                                                              @Valid @RequestBody CategoriaFormDto dto) {
        var actualizada = categoriaServicio.actualizar(id, dto);
        return RespuestaApiDto.ok("Categoría actualizada exitosamente", mapeador.aDtoSalida(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        // Si tiene productos asociados → ReglaNegocioException → 422, manejado de forma centralizada.
        categoriaServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
