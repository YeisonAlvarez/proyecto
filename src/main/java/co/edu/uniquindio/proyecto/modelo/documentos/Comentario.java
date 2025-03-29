package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "comentarios")  // Definir la colección si deseas almacenar comentarios como documentos individuales
public class Comentario {

    //Estos son los atributos de la clase Comentario
    private String idUsuario;   // ID del usuario que realiza el comentario
    private String comentario;  // El texto del comentario

    // Constructor
    public Comentario(String idUsuario, String comentario) {
        this.idUsuario = idUsuario;
        this.comentario = comentario;
    }
}
