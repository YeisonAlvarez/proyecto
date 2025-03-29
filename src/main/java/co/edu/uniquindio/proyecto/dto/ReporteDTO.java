package co.edu.uniquindio.proyecto.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDTO {

    String id;
    String nombre;
    String descripcion;
    String categoria;
    LocalDateTime fecha;
}
