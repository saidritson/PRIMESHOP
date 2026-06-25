package com.example.PRIMESHOP.controlador;

import com.example.PRIMESHOP.dto.InventarioItemDto;
import com.example.PRIMESHOP.servicio.InventarioServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión del inventario (stock por talla) en el panel de administración.
 *
 * Rutas base: /admin/inventario
 * Patrón: POST → Redirect → GET (PRG)
 */
@Controller
@RequestMapping("/admin/inventario")
public class AdminInventarioControlador {

    private final InventarioServicio inventarioServicio;

    public AdminInventarioControlador(InventarioServicio inventarioServicio) {
        this.inventarioServicio = inventarioServicio;
    }

    // ── GET: Listado de inventario ─────────────────────────────────────────

    /**
     * Muestra el inventario completo (productos con sus tallas y stocks).
     * Ruta: GET /admin/inventario
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", inventarioServicio.listarProductosConStock());
        model.addAttribute("productosStockBajo", inventarioServicio.listarProductosConStockBajo());
        model.addAttribute("productosAgotados", inventarioServicio.listarProductosAgotados());
        model.addAttribute("inventarioForm", new InventarioItemDto());
        return "AdminInventario";
    }

    // ── POST: Actualizar stock ─────────────────────────────────────────────

    /**
     * Actualiza el stock de una talla específica.
     * Ruta: POST /admin/inventario/actualizar
     */
    @PostMapping("/actualizar")
    public String actualizarStock(@Valid @ModelAttribute("inventarioForm") InventarioItemDto dto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Datos inválidos: verifique el stock ingresado.");
            return "redirect:/admin/inventario";
        }

        try {
            inventarioServicio.actualizarStock(dto.getProductoTallaId(), dto.getNuevoStock());
            redirectAttributes.addFlashAttribute("mensajeExito", "Stock actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar stock: " + e.getMessage());
        }

        return "redirect:/admin/inventario"; // PRG
    }
}
