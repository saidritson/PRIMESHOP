package com.example.PRIMESHOP.controlador;

import com.example.PRIMESHOP.dto.CategoriaFormDto;
import com.example.PRIMESHOP.servicio.CategoriaServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de categorías en el panel de administración.
 *
 * Rutas base: /admin/categorias
 * Patrón: POST → Redirect → GET (PRG)
 */
@Controller
@RequestMapping("/admin/categorias")
public class AdminCategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    public AdminCategoriaControlador(CategoriaServicio categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    // ── GET: Listado + formulario inline ───────────────────────────────────

    /**
     * Muestra el listado de categorías junto con el formulario de nueva categoría.
     * Ruta: GET /admin/categorias
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaServicio.listarTodas());
        // Si no viene de un redirect con errores, inicializar DTO vacío
        if (!model.containsAttribute("categoriaForm")) {
            model.addAttribute("categoriaForm", new CategoriaFormDto());
        }
        return "AdminCategorias";
    }

    // ── POST: Crear categoría ──────────────────────────────────────────────

    /**
     * Procesa la creación de una nueva categoría.
     * Ruta: POST /admin/categorias/nueva
     */
    @PostMapping("/nueva")
    public String crear(@Valid @ModelAttribute("categoriaForm") CategoriaFormDto dto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Propagar el DTO con errores para que el GET los muestre
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoriaForm", bindingResult);
            redirectAttributes.addFlashAttribute("categoriaForm", dto);
            return "redirect:/admin/categorias";
        }

        try {
            categoriaServicio.crear(dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Categoría creada exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            redirectAttributes.addFlashAttribute("categoriaForm", dto);
        }

        return "redirect:/admin/categorias"; // PRG
    }

    // ── GET: Formulario de edición ─────────────────────────────────────────

    /**
     * Muestra el formulario de edición de una categoría.
     * Ruta: GET /admin/categorias/{id}/editar
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormEdicion(@PathVariable int id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("categoriaForm")) {
                model.addAttribute("categoriaForm", categoriaServicio.toFormDto(id));
            }
            model.addAttribute("categorias", categoriaServicio.listarTodas());
            model.addAttribute("esEdicion", true);
            model.addAttribute("idEdicion", id);
            return "AdminCategorias";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Categoría no encontrada.");
            return "redirect:/admin/categorias";
        }
    }

    // ── POST: Actualizar categoría ─────────────────────────────────────────

    /**
     * Procesa la edición de una categoría existente.
     * Ruta: POST /admin/categorias/{id}/editar
     */
    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable int id,
                             @Valid @ModelAttribute("categoriaForm") CategoriaFormDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoriaForm", bindingResult);
            redirectAttributes.addFlashAttribute("categoriaForm", dto);
            return "redirect:/admin/categorias/" + id + "/editar";
        }

        try {
            categoriaServicio.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Categoría actualizada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/admin/categorias"; // PRG
    }

    // ── POST: Toggle activa/inactiva ───────────────────────────────────────

    /**
     * Activa o desactiva una categoría según su estado actual.
     * Ruta: POST /admin/categorias/{id}/toggle
     */
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            categoriaServicio.toggleActiva(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Estado de la categoría actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/admin/categorias"; // PRG
    }

    // ── POST: Eliminar ─────────────────────────────────────────────────────

    /**
     * Elimina una categoría si no tiene productos asociados.
     * Ruta: POST /admin/categorias/{id}/eliminar
     */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            categoriaServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Categoría eliminada.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/categorias"; // PRG
    }
}
