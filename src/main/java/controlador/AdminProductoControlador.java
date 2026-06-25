package com.example.PRIMESHOP.controlador;

import com.example.PRIMESHOP.dto.ProductoFormDto;
import com.example.PRIMESHOP.servicio.CategoriaServicio;
import com.example.PRIMESHOP.servicio.ProductoServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de productos en el panel de administración.
 *
 * Rutas base: /admin/productos
 *
 * Implementa el patrón POST → Redirect → GET (PRG) en todas las operaciones
 * de escritura para evitar el reenvío accidental al refrescar la página.
 */
@Controller
@RequestMapping("/admin/productos")
public class AdminProductoControlador {

    private final ProductoServicio productoServicio;
    private final CategoriaServicio categoriaServicio;

    public AdminProductoControlador(ProductoServicio productoServicio,
                                    CategoriaServicio categoriaServicio) {
        this.productoServicio = productoServicio;
        this.categoriaServicio = categoriaServicio;
    }

    // ── GET: Listado ───────────────────────────────────────────────────────

    /**
     * Muestra el listado de todos los productos.
     * Ruta: GET /admin/productos
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoServicio.listarTodos());
        return "AdminProductos";
    }

    // ── GET: Formulario de nuevo producto ──────────────────────────────────

    /**
     * Muestra el formulario vacío para crear un nuevo producto.
     * Ruta: GET /admin/productos/nuevo
     */
    @GetMapping("/nuevo")
    public String mostrarFormNuevo(Model model) {
        model.addAttribute("productoForm", new ProductoFormDto());
        model.addAttribute("categorias", categoriaServicio.listarActivas());
        model.addAttribute("esEdicion", false);
        return "AdminProductoForm";
    }

    // ── POST: Crear producto ───────────────────────────────────────────────

    /**
     * Procesa la creación de un nuevo producto.
     * Si la validación falla, regresa al formulario con los errores.
     * Si tiene éxito, redirige al listado (patrón PRG).
     * Ruta: POST /admin/productos/nuevo
     */
    @PostMapping("/nuevo")
    public String crear(@Valid @ModelAttribute("productoForm") ProductoFormDto dto,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", categoriaServicio.listarActivas());
            model.addAttribute("esEdicion", false);
            return "AdminProductoForm"; // regresa al form con errores
        }

        try {
            productoServicio.crear(dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto creado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear el producto: " + e.getMessage());
        }

        return "redirect:/admin/productos"; // PRG
    }

    // ── GET: Formulario de edición ─────────────────────────────────────────

    /**
     * Muestra el formulario precargado con los datos del producto a editar.
     * Ruta: GET /admin/productos/{id}/editar
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormEdicion(@PathVariable int id, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("productoForm", productoServicio.toFormDto(id));
            model.addAttribute("categorias", categoriaServicio.listarActivas());
            model.addAttribute("esEdicion", true);
            return "AdminProductoForm";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Producto no encontrado.");
            return "redirect:/admin/productos";
        }
    }

    // ── POST: Actualizar producto ──────────────────────────────────────────

    /**
     * Procesa la edición de un producto existente.
     * Ruta: POST /admin/productos/{id}/editar
     */
    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable int id,
                             @Valid @ModelAttribute("productoForm") ProductoFormDto dto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            dto.setId(id);
            model.addAttribute("categorias", categoriaServicio.listarActivas());
            model.addAttribute("esEdicion", true);
            return "AdminProductoForm";
        }

        try {
            productoServicio.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto actualizado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }

        return "redirect:/admin/productos"; // PRG
    }

    // ── POST: Desactivar (eliminación lógica) ─────────────────────────────

    /**
     * Desactiva (oculta) un producto sin eliminarlo de la base de datos.
     * Ruta: POST /admin/productos/{id}/desactivar
     */
    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            productoServicio.desactivar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto desactivado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/admin/productos"; // PRG
    }

    // ── POST: Activar ─────────────────────────────────────────────────────

    /**
     * Reactiva un producto previamente desactivado.
     * Ruta: POST /admin/productos/{id}/activar
     */
    @PostMapping("/{id}/activar")
    public String activar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            productoServicio.activar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto activado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }
        return "redirect:/admin/productos"; // PRG
    }
}
