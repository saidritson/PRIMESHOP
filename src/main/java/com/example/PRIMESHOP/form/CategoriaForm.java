package com.example.PRIMESHOP.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Objeto que viaja entre el formulario de categoria y el controlador,
 * con sus anotaciones de validacion (Spring Validation).
 */
public class CategoriaForm {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede pasar de 80 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripcion no puede pasar de 255 caracteres")
    private String descripcion;

    private boolean activa = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
