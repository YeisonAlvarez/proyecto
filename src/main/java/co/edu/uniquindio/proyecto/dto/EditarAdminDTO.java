package co.edu.uniquindio.proyecto.dto;

import org.hibernate.validator.constraints.Length;

public record EditarAdminDTO(

        String telefono,
        String ciudad,
        String direccion
) {}
