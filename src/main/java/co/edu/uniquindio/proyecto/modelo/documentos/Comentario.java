package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.CategoriaEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "comentarios")  // Definir la colección si deseas almacenar comentarios como documentos individuales
public class Comentario {


    @Id
    private String id; //  MongoDB lo maneja como ObjectId internamente
    private String idUsuario;   // ID del usuario que realiza el comentario
    private String idReporte;   // ID del usuario que realiza el comentario
    private String comentario;  // El texto del comentario
    private LocalDateTime fecha;


    @Builder
    public Comentario(String idUsuario, String idReporte, String comentario, LocalDateTime fecha) {
        this.idUsuario = idUsuario;
        this.idReporte = idReporte;
        this.comentario = comentario;
        this.fecha = fecha;

    }
}


