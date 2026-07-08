package com.example.PRIMESHOP.dto;

import Dominio.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para cambiar el estado de un pedido desde el panel de administración.
 */
public class ActualizarEstadoPedidoDto {

    @NotNull(message = "Debe seleccionar un estado")
    private EstadoPedido nuevoEstado;

    public EstadoPedido getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(EstadoPedido nuevoEstado) { this.nuevoEstado = nuevoEstado; }
}
