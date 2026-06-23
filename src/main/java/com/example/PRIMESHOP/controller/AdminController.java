package com.example.PRIMESHOP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String mostrarDashboard() {
        return "AdminDashboard";
    }

    @GetMapping("/admin/productos")
    public String mostrarProductos() {
        return "AdminProductos";
    }

    @GetMapping("/admin/inventario")
    public String mostrarInventario() {
        return "AdminInventario";
    }

    @GetMapping("/admin/ordenes")
    public String mostrarOrdenes() {
        return "AdminOrdenes";
    }

    @GetMapping("/admin/categorias")
    public String mostrarCategorias() {
        return "AdminCategorias";
    }
}
