package dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PedidoRequestDto {

    @NotNull(message = "Debe seleccionar una dirección de envío")
    private Integer idDireccion;

    @NotNull(message = "Debe seleccionar un metodo de pago")
    private Integer idPago;

    @NotEmpty(message = "El carrito no puede estar vacío")
    private List<DetallePedidoRequestDto> items;

    public Integer getIdDireccion() { return idDireccion; }
    public void setIdDireccion(Integer idDireccion) { this.idDireccion = idDireccion; }
    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public List<DetallePedidoRequestDto> getItems() { return items; }
    public void setItems(List<DetallePedidoRequestDto> items) { this.items = items; }

    public static class DetallePedidoRequestDto {
        @NotNull(message = "El ID de la talla del producto es obligatorio")
        private Integer idProductoTalla;

        @NotNull(message = "La cantidad es obligatoria")
        private Integer cantidad;

        public Integer getIdProductoTalla() { return idProductoTalla; }
        public void setIdProductoTalla(Integer idProductoTalla) { this.idProductoTalla = idProductoTalla; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}