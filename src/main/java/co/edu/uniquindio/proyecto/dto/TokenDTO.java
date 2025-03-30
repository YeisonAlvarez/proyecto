package co.edu.uniquindio.proyecto.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

public record TokenDTO(
        @NotBlank String token,
        @NotBlank @Pattern(regexp = "Bearer", message = "El tipo de token debe ser 'Bearer'")
        String tipo,
        @NotNull
        Instant expiracion
) {


}

