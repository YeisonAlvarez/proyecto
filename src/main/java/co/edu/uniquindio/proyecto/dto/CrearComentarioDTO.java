package co.edu.uniquindio.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
public record CrearComentarioDTO(
        @NotBlank String idUsuario,
        @NotBlank String idReporte,
        @NotBlank @Length(max = 500) String comentario,
        @NotNull LocalDateTime fecha
) {
}
