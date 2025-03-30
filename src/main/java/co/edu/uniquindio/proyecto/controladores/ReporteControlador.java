package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.CategoriaEnum;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reportes")
public class ReporteControlador {

    private final ReporteServicio reporteServicio;

    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crearReporte(@Valid @RequestBody CrearReporteDTO crearReporteDTO) {
        Reporte reporte = Reporte.builder()
                .nombre(crearReporteDTO.titulo())
                .descripcion(crearReporteDTO.descripcion())
                .categoria(CategoriaEnum.valueOf(crearReporteDTO.categoria().toUpperCase())) // Conversión segura a Enum
                .usuarioId(crearReporteDTO.idUsuario()) // Mantener como String
                .fechaCreacion(LocalDateTime.now())
                .build();

        Reporte reporteGuardado = reporteServicio.crearReporte(reporte);

        return ResponseEntity.status(201)
                .body(new MensajeDTO<String>(false, "Reporte creado exitosamente: " + reporteGuardado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reporte> reportesPage = reporteServicio.listarReportes(pageable);
        return ResponseEntity.ok(reportesPage.getContent());  // Devuelve solo el contenido de la página
    }


    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> obtenerReporte(@PathVariable String id) {

        Reporte reporte = reporteServicio.obtenerReportePorId(id);
        if (reporte == null) {
            return ResponseEntity.ok(new MensajeDTO<>(true, "Reporte no encontrado"));
        }
        return ResponseEntity.ok(new MensajeDTO<>(false,"Reporte encontrado"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> editarReporte(@PathVariable String id, @Valid @RequestBody EditarReporteDTO editarReporteDTO) {
        Reporte reporte = reporteServicio.obtenerReportePorId(id);
        if (reporte == null) {
            return ResponseEntity.ok(new MensajeDTO<>(true, "Reporte no encontrado"));
        }

        reporte.setNombre(editarReporteDTO.getNombre());
        reporte.setDescripcion(editarReporteDTO.getDescripcion());
        reporte.setCategoria(CategoriaEnum.valueOf(editarReporteDTO.getCategoria().toUpperCase()));

        reporteServicio.crearReporte(reporte); // Guardar los cambios

        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarReporte(@PathVariable String id) {
        reporteServicio.eliminarReporte(id);
        return ResponseEntity.ok(new MensajeDTO<String>(false, "Reporte eliminado exitosamente"));
    }
}

