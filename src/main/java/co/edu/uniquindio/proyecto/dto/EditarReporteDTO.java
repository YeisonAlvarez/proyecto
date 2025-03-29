package co.edu.uniquindio.proyecto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarReporteDTO {

    private String nombre;
    private String descripcion;
    private String categoria;

    public EditarReporteDTO(String nombre, String descripcion, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }
}
