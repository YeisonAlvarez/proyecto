package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("comentario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comentario {

    @Id
    private String id;

    private String comentarioTexto;

    private LocalDateTime fecha;

    private String usuarioId;

    private String reporteId;
}
