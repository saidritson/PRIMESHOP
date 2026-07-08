package com.example.PRIMESHOP.controller;

import com.example.PRIMESHOP.service.CategoriaService;
import com.example.PRIMESHOP.service.PedidoService;
import com.example.PRIMESHOP.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador del panel de resumen (dashboard) del administrador.
 */
@Controller
public class DashboardController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final CategoriaService categoriaService;

    public DashboardController(ProductoService productoService, PedidoService pedidoService,
            CategoriaService categoriaService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("ventasDia", pedidoService.ventasDelDia());
        model.addAttribute("ordenesPendientes", pedidoService.contarPendientes());
        model.addAttribute("productosActivos", productoService.contarActivos());
        model.addAttribute("totalCategorias", categoriaService.contar());
        model.addAttribute("stockBajo", productoService.stockBajo());
        model.addAttribute("ultimasOrdenes", pedidoService.ultimas(5));
        return "AdminDashboard";
    }
}
