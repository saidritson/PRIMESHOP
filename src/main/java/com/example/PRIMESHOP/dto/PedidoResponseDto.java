package com.example.PRIMESHOP.dto;

import Dominio.enums.EstadoPago;
import Dominio.enums.EstadoPedido;
import Dominio.enums.MetodoPago;
import java.util.Date;
import java.util.List;

/**
 * Respuesta enviada al cliente tras generar un pedido (checkout) o al
 * consultar el historial de ordenes propio.
 */
public class PedidoResponseDto {

    private int id;
    private String numeroPedido;
    private Date fecha;
    private double total;
    private EstadoPedido estado;
    private MetodoPago metodoPago;
    private EstadoPago estadoPago;
    private List<DetallePedidoResponseDto> detalles;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }

    public List<DetallePedidoResponseDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoResponseDto> detalles) { this.detalles = detalles; }
}
