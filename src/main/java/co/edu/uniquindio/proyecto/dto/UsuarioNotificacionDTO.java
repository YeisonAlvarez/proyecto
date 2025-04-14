package co.edu.uniquindio.proyecto.dto;

import org.bson.types.ObjectId;

public record UsuarioNotificacionDTO(
        ObjectId id,
        String email,
        boolean notificacionApp,
        boolean notificacionEmail
) {}