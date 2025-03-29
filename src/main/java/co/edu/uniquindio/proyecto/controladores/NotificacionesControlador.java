package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.NotificacionDTO;
import co.edu.uniquindio.proyecto.dto.NotificacionSuscripcionDTO;
import co.edu.uniquindio.proyecto.servicios.NotificacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api") // El prefijo común para todas las rutas del controlador
public class NotificacionesControlador {

    @Autowired
    private NotificacionServicio notificacionServicio;

    // Obtener las notificaciones cercanas a un usuario
    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(
            @RequestParam String idUsuario,
            @RequestParam double latitud,
            @RequestParam double longitud) {

        // Llamar al servicio para obtener las notificaciones cercanas
        List<NotificacionDTO> notificaciones = notificacionServicio.obtenerNotificaciones(idUsuario, latitud, longitud);

        // Retornar la lista de notificaciones con código HTTP 200 (OK)
        return ResponseEntity.ok(notificaciones);
    }

    // Activar o desactivar las notificaciones
    @PostMapping("/notificaciones/suscribirse")
    public ResponseEntity<MensajeDTO<String>> activarNotificaciones(@Valid @RequestBody NotificacionSuscripcionDTO notificacionSuscripcionDTO) {

        // Llamar al servicio para activar o desactivar las notificaciones
        boolean suscrito = notificacionServicio.activarSuscripcion(notificacionSuscripcionDTO);

        // Retornar mensaje dependiendo si fue exitosa la operación
        if (suscrito) {
            return ResponseEntity.ok(new MensajeDTO<>(false, "Notificaciones activadas exitosamente"));
        } else {
            return ResponseEntity.status(400).body(new MensajeDTO<>(true, "No se pudo activar las notificaciones"));
        }
    }

    // Obtener el detalle de una notificación específica
    @GetMapping("/notificaciones/{id}")
    public ResponseEntity<NotificacionDTO> obtenerDetalleNotificacion(@PathVariable String id) {

        // Llamar al servicio para obtener el detalle de la notificación
        NotificacionDTO notificacion = notificacionServicio.obtenerDetalle(id);

        // Si la notificación existe, retornar con código HTTP 200 (OK)
        if (notificacion != null) {
            return ResponseEntity.ok(notificacion);
        } else {
            // Si no se encuentra, retornar código HTTP 404 (Not Found)
            return ResponseEntity.status(404).body(null);
        }
    }

}
