package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.NotificacionDTO;
import co.edu.uniquindio.proyecto.dto.NotificacionSuscripcionDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Notificacion;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.repositorios.NotificacionRepositorio;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.NotificacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionServicioImpl implements NotificacionServicio {

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;

    @Autowired
    private UsuarioRepo usuarioRepositorio;

    @Override
    public List<NotificacionDTO> obtenerNotificaciones(String idUsuario, double latitud, double longitud) {
        // Ajusta los valores para un rango cercano (por ejemplo, ±0.01 grados)
        double latMin = latitud - 0.01;
        double latMax = latitud + 0.01;
        double lonMin = longitud - 0.01;
        double lonMax = longitud + 0.01;

        List<Notificacion> notificaciones = notificacionRepositorio.findByUsuarioIdAndLatitudBetweenAndLongitudBetween(idUsuario, latMin, latMax, lonMin, lonMax);

        List<NotificacionDTO> notificacionDTOs = new ArrayList<>();
        for (Notificacion notificacion : notificaciones) {
            notificacionDTOs.add(new NotificacionDTO(notificacion.getMensaje(), notificacion.getLatitud(), notificacion.getLongitud()));
        }

        return notificacionDTOs;
    }
    // Activar o desactivar la suscripción a las notificaciones
    @Override
    public boolean activarSuscripcion(NotificacionSuscripcionDTO suscripcionDTO) {
        // Convertir el ID de usuario de String a ObjectId
        ObjectId usuarioId = new ObjectId(suscripcionDTO.getIdUsuario());

        // Buscar el usuario por el ID convertido
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Activar/desactivar las notificaciones
        usuario.setNotificacionApp(suscripcionDTO.isNotificacionApp());
        usuario.setNotificacionEmail(suscripcionDTO.isNotificacionEmail());
        usuarioRepositorio.save(usuario);

        return true;
    }

    // Obtener el detalle de una notificación por su ID
    @Override
    public NotificacionDTO obtenerDetalle(String id) {
        // Buscar la notificación
        Notificacion notificacion = notificacionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        // Convertir a DTO
        return new NotificacionDTO(notificacion.getMensaje(), notificacion.getLatitud(), notificacion.getLongitud());
    }


}
