package Dominio.enums;

public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    PROCESANDO("Procesando"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
