package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.NotificacionDTO;
import co.edu.uniquindio.proyecto.dto.NotificacionSuscripcionDTO;

import java.util.List;

public interface NotificacionServicio {

    // Obtener las notificaciones cercanas para un usuario
    List<NotificacionDTO> obtenerNotificaciones(String idUsuario, double latitud, double longitud);

    // Activar/desactivar suscripción a notificaciones
    boolean activarSuscripcion(NotificacionSuscripcionDTO suscripcionDTO);

    // Obtener el detalle de una notificación
    NotificacionDTO obtenerDetalle(String id);
}
