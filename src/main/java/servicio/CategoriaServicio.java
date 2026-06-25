package com.example.PRIMESHOP.servicio;

import Dominio.Categoria;
import com.example.PRIMESHOP.dto.CategoriaFormDto;
import com.example.PRIMESHOP.repositorio.CategoriaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio de negocio para la gestión de categorías.
 */
@Service
@Transactional
public class CategoriaServicio {

    private final CategoriaRepositorio categoriaRepo;

    public CategoriaServicio(CategoriaRepositorio categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    // ── Consultas ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarActivas() {
        return categoriaRepo.findByActiva(true);
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(int id) {
        return categoriaRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con id: " + id));
    }

    // ── Creación ───────────────────────────────────────────────────────────

    public Categoria crear(CategoriaFormDto dto) {
        validarNombreUnico(dto.getNombre(), null);
        Categoria categoria = new Categoria(dto.getNombre(), dto.getDescripcion());
        categoria.setActiva(dto.isActiva());
        return categoriaRepo.save(categoria);
    }

    // ── Edición ────────────────────────────────────────────────────────────

    public Categoria actualizar(int id, CategoriaFormDto dto) {
        validarNombreUnico(dto.getNombre(), id);
        Categoria categoria = buscarPorId(id);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setActiva(dto.isActiva());
        return categoriaRepo.save(categoria);
    }

    // ── Activar / Desactivar ───────────────────────────────────────────────

    public void toggleActiva(int id) {
        Categoria categoria = buscarPorId(id);
        categoria.setActiva(!categoria.isActiva());
        categoriaRepo.save(categoria);
    }

    // ── Eliminación ────────────────────────────────────────────────────────

    public void eliminar(int id) {
        Categoria categoria = buscarPorId(id);
        if (categoria.getCantidadProductos() > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoría '" + categoria.getNombre() +
                    "' porque tiene " + categoria.getCantidadProductos() + " producto(s) asociado(s).");
        }
        categoriaRepo.delete(categoria);
    }

    // ── Conversión ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public CategoriaFormDto toFormDto(int id) {
        Categoria c = buscarPorId(id);
        CategoriaFormDto dto = new CategoriaFormDto();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        dto.setActiva(c.isActiva());
        return dto;
    }

    // ── Privados ───────────────────────────────────────────────────────────

    private void validarNombreUnico(String nombre, Integer idExcluido) {
        if (idExcluido == null) {
            if (categoriaRepo.findByNombreIgnoreCase(nombre).isPresent()) {
                throw new IllegalArgumentException("Ya existe una categoría con el nombre '" + nombre + "'");
            }
        } else {
            if (categoriaRepo.existsByNombreIgnoreCaseAndIdNot(nombre, idExcluido)) {
                throw new IllegalArgumentException("Ya existe otra categoría con el nombre '" + nombre + "'");
            }
        }
    }
}
