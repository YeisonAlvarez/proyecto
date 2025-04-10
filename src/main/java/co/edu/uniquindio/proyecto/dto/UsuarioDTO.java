package co.edu.uniquindio.proyecto.dto;

import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;

public record UsuarioDTO(
        String id,
        String nombre,
        Ciudad ciudad,
        String direccion,
        String telefono,
        String email,
        EstadoUsuario estado
) {
}
