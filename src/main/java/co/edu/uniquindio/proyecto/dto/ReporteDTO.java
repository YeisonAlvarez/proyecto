package co.edu.uniquindio.proyecto.dto;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import lombok.*;

import java.time.LocalDateTime;

public record ReporteDTO(
        String id,
        String nombre,
        String descripcion,
        String categoria,
        LocalDateTime fechaCreacion,
        EstadoReporte estado,
        UbicacionDTO ubicacionDTO,
        int conteoImportantes



) {}

