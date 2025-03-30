package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.documentos.Ubicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReporteServicio {


    Reporte crearReporte(Reporte reporte);
    void actualizarReporte(String id, EditarReporteDTO editarReporteDTO);
    void eliminarReporte(String id);
    Reporte obtenerReportePorId(String id);
    List<InfoReporteDTO> obtenerReporte(String categoria, EstadoReporteDTO estadoReporteDTO);
    List<InfoReporteDTO>  obtenerReportesUsuario(String idUsuario);
    List<InfoReporteDTO>  obtenerReportesUbicacion(Ubicacion ubicacion);

    Page<Reporte> listarReportes(Pageable pageable); // Método paginado


    boolean marcarImportante(String idReporte, UsuarioDTO usuarioDTO);
    boolean cambiarEstado(String idReporte, EstadoReporteDTO estadoReporteDTO);




    //Optional<HistorialEstadoDTO> listarHistorialEstados(String id);

}
