package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.Categoria;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
@Document("reportes")
public class Reporte {

    @Id
    private String id; //  MongoDB lo maneja como ObjectId internamente
// atributos de la clase reporte
    private String nombre;
    private String descripcion;
    private Categoria categoria;
    private String usuarioId; //  Mantener como String
    private LocalDateTime fechaCreacion;
    private boolean importante; // Propiedad para marcar como importante
    private EstadoReporte estado; // Propiedad para el estado del reporte
    private String motivoCambioEstado; // Motivo del cambio de estado
    private List<Comentario> comentarios = new ArrayList<>();  // Inicializamos la lista aquí


    @Builder
    public Reporte(String nombre, String descripcion, Categoria categoria, String usuarioId, LocalDateTime fechaCreacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.usuarioId = usuarioId;
        this.fechaCreacion = fechaCreacion;
        this.importante = importante;
        this.estado = estado;
        this.motivoCambioEstado = motivoCambioEstado;
        this.comentarios = comentarios;

    }
}
