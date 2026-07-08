package com.example.PRIMESHOP.controller;

import Dominio.Producto;
import com.example.PRIMESHOP.form.ProductoForm;
import com.example.PRIMESHOP.service.CategoriaService;
import com.example.PRIMESHOP.service.ProductoService;
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
 * Controlador del CRUD de productos del administrador.
 * Usa el patron POST/Redirect/GET en cada accion de escritura.
 */
@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "AdminProductos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("productoForm", new ProductoForm());
        model.addAttribute("categorias", categoriaService.listar());
        return "AdminProductoForm";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        Producto producto = productoService.buscar(id);
        if (producto == null) {
            return "redirect:/admin/productos";
        }
        model.addAttribute("productoForm", productoService.aFormulario(producto));
        model.addAttribute("categorias", categoriaService.listar());
        return "AdminProductoForm";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("productoForm") ProductoForm form,
            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listar());
            return "AdminProductoForm";
        }
        productoService.guardar(form);
        ra.addFlashAttribute("mensaje", "Producto guardado correctamente");
        return "redirect:/admin/productos";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable int id, RedirectAttributes ra) {
        productoService.cambiarEstado(id);
        ra.addFlashAttribute("mensaje", "Estado del producto actualizado");
        return "redirect:/admin/productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes ra) {
        productoService.eliminar(id);
        ra.addFlashAttribute("mensaje", "Producto eliminado");
        return "redirect:/admin/productos";
    }
}
