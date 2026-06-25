package com.example.PRIMESHOP.controlador;

import Dominio.enums.EstadoPedido;
import com.example.PRIMESHOP.servicio.CategoriaServicio;
import com.example.PRIMESHOP.servicio.InventarioServicio;
import com.example.PRIMESHOP.servicio.PedidoServicio;
import com.example.PRIMESHOP.servicio.ProductoServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador del Dashboard principal de administración.
 *
 * Ruta: GET /admin/dashboard
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardControlador {

    private final ProductoServicio productoServicio;
    private final CategoriaServicio categoriaServicio;
    private final PedidoServicio pedidoServicio;
    private final InventarioServicio inventarioServicio;

    public AdminDashboardControlador(ProductoServicio productoServicio,
                                     CategoriaServicio categoriaServicio,
                                     PedidoServicio pedidoServicio,
                                     InventarioServicio inventarioServicio) {
        this.productoServicio = productoServicio;
        this.categoriaServicio = categoriaServicio;
        this.pedidoServicio = pedidoServicio;
        this.inventarioServicio = inventarioServicio;
    }

    /**
     * Carga las estadísticas principales para el panel del administrador.
     * Ruta: GET /admin/dashboard
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Totales de catálogo
        model.addAttribute("totalProductos", productoServicio.listarTodos().size());
        model.addAttribute("totalCategorias", categoriaServicio.listarTodas().size());

        // Totales de pedidos por estado
        model.addAttribute("pedidosPendientes", pedidoServicio.contarPorEstado(EstadoPedido.PENDIENTE));
        model.addAttribute("pedidosProcesando", pedidoServicio.contarPorEstado(EstadoPedido.PROCESANDO));
        model.addAttribute("pedidosEnviados", pedidoServicio.contarPorEstado(EstadoPedido.ENVIADO));
        model.addAttribute("pedidosEntregados", pedidoServicio.contarPorEstado(EstadoPedido.ENTREGADO));

        // Ventas totales (pedidos entregados)
        model.addAttribute("totalVentas", pedidoServicio.calcularTotalVentas());

        // Alertas de inventario
        model.addAttribute("productosStockBajo", inventarioServicio.listarProductosConStockBajo());
        model.addAttribute("productosAgotados", inventarioServicio.listarProductosAgotados());

        // Últimos pedidos (todos, la vista mostrará los N primeros)
        model.addAttribute("ultimosPedidos", pedidoServicio.listarTodos());

        return "AdminDashboard";
    }
}
