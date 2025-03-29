package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.EstadoReporteDTO;
import co.edu.uniquindio.proyecto.dto.UsuarioDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReporteServicio {
    Reporte crearReporte(Reporte reporte);
    List<Reporte> listarReportes();
    Page<Reporte> listarReportes(Pageable pageable); // Método paginado
    Optional<Reporte> obtenerReportePorId(String id);
    void eliminarReporte(String id);
    boolean agregarComentario(String idReporte, ComentarioDTO comentarioDTO);
    boolean marcarImportante(String idReporte, UsuarioDTO usuarioDTO);
    boolean cambiarEstado(String idReporte, EstadoReporteDTO estadoReporteDTO);

}
