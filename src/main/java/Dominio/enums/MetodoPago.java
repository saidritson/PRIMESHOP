package Dominio.enums;

public enum MetodoPago {
    TARJETA("Tarjeta de credito"),
    CONTRA_ENTREGA("Pago contra entrega"),
    TRANSFERENCIA("Transferencia bancaria");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
