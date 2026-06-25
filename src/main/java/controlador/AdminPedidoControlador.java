package com.example.PRIMESHOP.controlador;

import Dominio.enums.EstadoPedido;
import com.example.PRIMESHOP.dto.ActualizarEstadoPedidoDto;
import com.example.PRIMESHOP.servicio.PedidoServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de pedidos en el panel de administración.
 *
 * Rutas base: /admin/ordenes
 * Patrón: POST → Redirect → GET (PRG)
 */
@Controller
@RequestMapping("/admin/ordenes")
public class AdminPedidoControlador {

    private final PedidoServicio pedidoServicio;

    public AdminPedidoControlador(PedidoServicio pedidoServicio) {
        this.pedidoServicio = pedidoServicio;
    }

    // ── GET: Listado de pedidos ────────────────────────────────────────────

    /**
     * Muestra todos los pedidos, con filtro opcional por estado.
     * Ruta: GET /admin/ordenes
     * Ruta: GET /admin/ordenes?estado=PENDIENTE
     */
    @GetMapping
    public String listar(@RequestParam(required = false) EstadoPedido estado, Model model) {
        if (estado != null) {
            model.addAttribute("pedidos", pedidoServicio.listarPorEstado(estado));
            model.addAttribute("filtroEstado", estado);
        } else {
            model.addAttribute("pedidos", pedidoServicio.listarTodos());
        }
        model.addAttribute("estadosPosibles", EstadoPedido.values());
        model.addAttribute("estadoForm", new ActualizarEstadoPedidoDto());
        return "AdminOrdenes";
    }

    // ── GET: Detalle de pedido ─────────────────────────────────────────────

    /**
     * Muestra el detalle completo de un pedido.
     * Ruta: GET /admin/ordenes/{id}
     */
    @GetMapping("/{id}")
    public String detalle(@PathVariable int id, Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("pedido", pedidoServicio.buscarPorId(id));
            model.addAttribute("estadosPosibles", EstadoPedido.values());
            model.addAttribute("estadoForm", new ActualizarEstadoPedidoDto());
            return "DetallePedido";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Pedido no encontrado.");
            return "redirect:/admin/ordenes";
        }
    }

    // ── POST: Cambiar estado ───────────────────────────────────────────────

    /**
     * Actualiza el estado de un pedido.
     * Ruta: POST /admin/ordenes/{id}/estado
     */
    @PostMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable int id,
                                   @Valid @ModelAttribute("estadoForm") ActualizarEstadoPedidoDto dto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debe seleccionar un estado válido.");
            return "redirect:/admin/ordenes/" + id;
        }

        try {
            pedidoServicio.actualizarEstado(id, dto.getNuevoEstado());
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Estado actualizado a: " + dto.getNuevoEstado().getDescripcion());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar estado: " + e.getMessage());
        }

        return "redirect:/admin/ordenes/" + id; // PRG
    }
}
