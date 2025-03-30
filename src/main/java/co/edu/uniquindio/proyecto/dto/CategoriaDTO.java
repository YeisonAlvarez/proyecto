package co.edu.uniquindio.proyecto.dto;

import co.edu.uniquindio.proyecto.modelo.enums.CategoriaEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
        @NotNull String nombre,
        @NotBlank String descripcion
) {
}
