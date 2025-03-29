package co.edu.uniquindio.proyecto.modelo.documentos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "notificaciones")
public class Notificacion {

    @Id
    private String id;

    private String usuarioId; // ID del usuario que recibe la notificación

    private double latitud;  // Latitud de la notificación
    private double longitud; // Longitud de la notificación

    private String mensaje;  // El mensaje de la notificación

    // Otros campos que consideres necesarios
}
