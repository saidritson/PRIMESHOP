package com.example.PRIMESHOP.controller;

import Dominio.Categoria;
import com.example.PRIMESHOP.form.CategoriaForm;
import com.example.PRIMESHOP.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador del CRUD de categorias del administrador (POST/Redirect/GET).
 */
@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        if (!model.containsAttribute("categoriaForm")) {
            model.addAttribute("categoriaForm", new CategoriaForm());
        }
        model.addAttribute("categorias", categoriaService.listar());
        return "AdminCategorias";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        Categoria categoria = categoriaService.buscar(id);
        if (categoria == null) {
            return "redirect:/admin/categorias";
        }
        CategoriaForm form = new CategoriaForm();
        form.setId(categoria.getId());
        form.setNombre(categoria.getNombre());
        form.setDescripcion(categoria.getDescripcion());
        form.setActiva(categoria.isActiva());
        model.addAttribute("categoriaForm", form);
        model.addAttribute("categorias", categoriaService.listar());
        return "AdminCategorias";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("categoriaForm") CategoriaForm form,
            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listar());
            return "AdminCategorias";
        }
        categoriaService.guardar(form);
        ra.addFlashAttribute("mensaje", "Categoria guardada correctamente");
        return "redirect:/admin/categorias";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable int id, RedirectAttributes ra) {
        categoriaService.cambiarEstado(id);
        ra.addFlashAttribute("mensaje", "Estado de la categoria actualizado");
        return "redirect:/admin/categorias";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes ra) {
        categoriaService.eliminar(id);
        ra.addFlashAttribute("mensaje", "Categoria eliminada");
        return "redirect:/admin/categorias";
    }
}
