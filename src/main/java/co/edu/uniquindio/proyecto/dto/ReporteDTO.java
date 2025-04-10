package co.edu.uniquindio.proyecto.dto;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public record ReporteDTO(
        String id,
        String nombre,
        String descripcion,
        String categoria,
        LocalDateTime fechaCreacion,
        EstadoReporte estado,
        UbicacionDTO ubicacionDTO,
        int conteoImportantes,


        double promedioEstrellas,
        List<Integer> calificaciones



) {}

