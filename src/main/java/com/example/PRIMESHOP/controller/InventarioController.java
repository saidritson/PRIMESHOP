package com.example.PRIMESHOP.controller;

import com.example.PRIMESHOP.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador del control de inventario (ajuste de existencias).
 */
@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {

    private final ProductoService productoService;

    public InventarioController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String inventario(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "AdminInventario";
    }

    @PostMapping("/{id}/ajustar")
    public String ajustar(@PathVariable int id, @RequestParam int cantidad, RedirectAttributes ra) {
        productoService.ajustarStock(id, cantidad);
        ra.addFlashAttribute("mensaje", "Inventario actualizado");
        return "redirect:/admin/inventario";
    }
}
