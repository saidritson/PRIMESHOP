package Dominio.enums;

public enum RolUsuario {
    CLIENTE("Cliente"),
    ADMINISTRADOR("Administrador");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
