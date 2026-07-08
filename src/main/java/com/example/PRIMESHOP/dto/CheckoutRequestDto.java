package com.example.PRIMESHOP.dto;

import Dominio.enums.MetodoPago;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo de la peticion para confirmar la compra (checkout). Toma el
 * contenido actual del carrito del usuario autenticado, la direccion de
 * envio elegida y el metodo de pago.
 */
public class CheckoutRequestDto {

    @NotNull(message = "Debe seleccionar una direccion de envio")
    private Integer direccionId;

    @NotNull(message = "Debe seleccionar un metodo de pago")
    private MetodoPago metodoPago;

    public Integer getDireccionId() { return direccionId; }
    public void setDireccionId(Integer direccionId) { this.direccionId = direccionId; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
}
