package co.edu.uniquindio.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDTO {
    private String mensaje;
    private double latitud;
    private double longitud;

    //private String titulo;
    //private String cuerpo;
    //private String topic;

}
