package com.example.PRIMESHOP.controller;

import Dominio.enums.EstadoPedido;
import com.example.PRIMESHOP.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de la gestion de ordenes: listado, filtro por estado
 * y cambio de estado (pendiente -> procesando -> enviado -> entregado).
 */
@Controller
@RequestMapping("/admin/ordenes")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoPedido estado, Model model) {
        model.addAttribute("pedidos",
                (estado == null) ? pedidoService.listar() : pedidoService.listarPorEstado(estado));
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("estadoActivo", estado);
        return "AdminOrdenes";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable int id, @RequestParam EstadoPedido estado,
            RedirectAttributes ra) {
        pedidoService.cambiarEstado(id, estado);
        ra.addFlashAttribute("mensaje", "Estado de la orden actualizado");
        return "redirect:/admin/ordenes";
    }
}
