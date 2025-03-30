package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.mapper.ComentarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.repositorios.ComentarioRepo;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.servicios.ComentarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComentarioServicioImpl implements ComentarioServicio {

    private final ComentarioRepo comentarioRepo;
    private final ReporteRepo reporteRepo;
    private final ComentarioMapper comentarioMapper;

    @Override
    public List<ComentarioDTO> obtenerComentarios(String idReporte) {
        return reporteRepo.findById(idReporte)
                .map(Reporte::getComentarios) // Obtiene la lista de comentarios del reporte
                .orElse(Collections.emptyList()) // Si no hay reporte, devuelve una lista vacía
                .stream()
                .map(comentarioMapper::toDTO) // Convierte los comentarios a DTOs
                .collect(Collectors.toList());
    }

    @Override
    public void crearComentario(String idReporte, CrearComentarioDTO crearComentarioDTO) {
        Optional<Reporte> reporteOpt = reporteRepo.findById(idReporte);
        if (reporteOpt.isPresent()) {
            Reporte reporte = reporteOpt.get();
            Comentario comentario = comentarioMapper.toDocument(crearComentarioDTO);
            reporte.getComentarios().add(comentario);
            reporteRepo.save(reporte);
        }
    }
}