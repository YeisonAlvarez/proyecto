package co.edu.uniquindio.proyecto.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioDTO {

    String id;
    private String idUsuario;
    private String idReporte;
    private String comentario;
    private LocalDateTime fecha;
}
