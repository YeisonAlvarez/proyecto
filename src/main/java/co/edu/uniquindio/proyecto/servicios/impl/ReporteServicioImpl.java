package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.EstadoReporteDTO;
import co.edu.uniquindio.proyecto.dto.UsuarioDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReporteServicioImpl implements ReporteServicio {

    private final ReporteRepo reporteRepo;  // Asegúrate de usar reporteRepo (inyectado correctamente)

    @Override
    public Reporte crearReporte(Reporte reporte) {
        return reporteRepo.save(reporte);
    }

    @Override
    public List<Reporte> listarReportes() {
        return reporteRepo.findAll();
    }

    @Override
    public Page<Reporte> listarReportes(Pageable pageable) {
        return reporteRepo.findAll(pageable);
    }

    @Override
    public Optional<Reporte> obtenerReportePorId(String id) {
        return reporteRepo.findById(id);
    }

    @Override
    public void eliminarReporte(String id) {
        reporteRepo.deleteById(id);
    }

    @Override
    public boolean agregarComentario(String idReporte, ComentarioDTO comentarioDTO) {
        // Cambié "reporteRepositorio" por "reporteRepo"
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        // Crear el comentario y añadirlo al reporte
        Comentario comentario = new Comentario(comentarioDTO.getIdUsuario(), comentarioDTO.getComentario());
        reporte.getComentarios().add(comentario);

        // Guardamos el reporte actualizado
        reporteRepo.save(reporte);
        return true;
    }

    @Override
    public boolean marcarImportante(String idReporte, UsuarioDTO usuarioDTO) {
        // Cambié "reporteRepositorio" por "reporteRepo"
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        // Lógica para marcar como importante
        reporte.setImportante(true);
        reporteRepo.save(reporte);

        return true;
    }

    @Override
    public boolean cambiarEstado(String idReporte, EstadoReporteDTO estadoReporteDTO) {
        // Cambié "reporteRepositorio" por "reporteRepo"
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        // Lógica para cambiar el estado
        // Convertimos el String a Enum (asegúrate de que "EstadoReporte" sea un Enum válido)
        reporte.setEstado(EstadoReporte.valueOf(estadoReporteDTO.getNuevoEstado())); // Usamos Enum.valueOf
        reporte.setMotivoCambioEstado(estadoReporteDTO.getMotivo());
        reporteRepo.save(reporte);

        return true;
    }
}
