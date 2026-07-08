package com.example.PRIMESHOP.config;

import Dominio.Categoria;
import Dominio.DetallePedido;
import Dominio.Direccion;
import Dominio.Pedido;
import Dominio.Producto;
import Dominio.ProductoTalla;
import Dominio.Usuario;
import Dominio.enums.EstadoPedido;
import Dominio.enums.RolUsuario;
import com.example.PRIMESHOP.repository.CategoriaRepository;
import com.example.PRIMESHOP.repository.DetallePedidoRepository;
import com.example.PRIMESHOP.repository.DireccionRepository;
import com.example.PRIMESHOP.repository.PedidoRepository;
import com.example.PRIMESHOP.repository.ProductoRepository;
import com.example.PRIMESHOP.repository.ProductoTallaRepository;
import com.example.PRIMESHOP.repository.UsuarioRepository;
import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inserta datos de prueba (categorias, productos con stock variado y una
 * orden de ejemplo) la primera vez que arranca, para poder probar el panel.
 * Solo siembra si todavia no hay categorias.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final ProductoTallaRepository tallaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;

    public DataSeeder(CategoriaRepository categoriaRepository, ProductoRepository productoRepository,
            ProductoTallaRepository tallaRepository, UsuarioRepository usuarioRepository,
            DireccionRepository direccionRepository, PedidoRepository pedidoRepository,
            DetallePedidoRepository detalleRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.tallaRepository = tallaRepository;
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.pedidoRepository = pedidoRepository;
        this.detalleRepository = detalleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoriaRepository.count() > 0) {
            return;
        }

        Categoria playera = new Categoria("Playera", "Playeras y camisetas");
        Categoria tenis = new Categoria("Tenis", "Calzado deportivo");
        Categoria sudadera = new Categoria("Sudadera", "Sudaderas y hoodies");
        categoriaRepository.saveAll(List.of(playera, tenis, sudadera));

        crearProducto("Playera Basica", "Playera de algodon", 180, "Negro", 5, 30, playera);
        Producto tenisAir = crearProducto("Tenis Air", "Tenis deportivos", 500, "Blanco", 5, 3, tenis);
        crearProducto("Sudadera Oversize", "Sudadera holgada", 450, "Gris", 5, 24, sudadera);
        crearProducto("Gorra Negra", "Gorra ajustable", 210, "Negro", 5, 0, playera);

        Usuario cliente = new Usuario("Cliente Demo", "cliente@correo.com", "demo", "6440000000", RolUsuario.CLIENTE);
        usuarioRepository.save(cliente);

        Direccion dir = new Direccion("Av. Tecnologico 1111", "Centro", "Obregon", "Sonora",
                "85000", "Porton negro", true, cliente);
        direccionRepository.save(dir);

        Pedido pedido = new Pedido("PS-1042", new Date(), 0, EstadoPedido.PENDIENTE, cliente, dir);
        pedidoRepository.save(pedido);

        DetallePedido detalle = new DetallePedido(1, tenisAir.getPrecio(), tenisAir, pedido);
        detalleRepository.save(detalle);

        pedido.setTotal(detalle.getSubtotal());
        pedidoRepository.save(pedido);
    }

    private Producto crearProducto(String nombre, String descripcion, double precio, String color,
            int stockMinimo, int stock, Categoria categoria) {
        Producto producto = new Producto(nombre, descripcion, precio, "", color, stockMinimo, categoria);
        productoRepository.save(producto);
        tallaRepository.save(new ProductoTalla("Unica", stock, producto));
        return producto;
    }
}
