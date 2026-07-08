package com.example.PRIMESHOP.service;

import Dominio.Categoria;
import com.example.PRIMESHOP.form.CategoriaForm;
import com.example.PRIMESHOP.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio para la administracion de categorias.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria buscar(int id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public long contar() {
        return categoriaRepository.count();
    }

    public void guardar(CategoriaForm form) {
        Categoria categoria = (form.getId() == null)
                ? new Categoria()
                : categoriaRepository.findById(form.getId()).orElse(new Categoria());

        categoria.setNombre(form.getNombre());
        categoria.setDescripcion(form.getDescripcion());
        categoria.setActiva(form.isActiva());
        categoriaRepository.save(categoria);
    }

    public void cambiarEstado(int id) {
        categoriaRepository.findById(id).ifPresent(c -> {
            c.setActiva(!c.isActiva());
            categoriaRepository.save(c);
        });
    }

    public void eliminar(int id) {
        categoriaRepository.deleteById(id);
    }
}
