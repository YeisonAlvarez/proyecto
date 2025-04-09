package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.mapper.ReporteMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;  // Para iText
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServicioImpl implements ReporteServicio {

    private final ReporteRepo reporteRepo;
    private final ReporteMapper reporteMapper;
    private final WebSocketNotificationService webSocketNotificationService;
    private final EmailServicio emailServicio;

    @Override
    public Reporte crearReporte(CrearReporteDTO crearReporteDTO) throws Exception{

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuarioAutenticado = user.getUsername();

        // Verificar que el usuario autenticado solo pueda modificar su propia cuenta
        if (!crearReporteDTO.idUsuario().equals(idUsuarioAutenticado)) {
            throw new IllegalAccessException("El id del usuario del login no coincide con el id del creador del reporte.");
        }

        // Crear el punto GeoJSON a partir de la ubicación recibida
        GeoJsonPoint geoPoint = new GeoJsonPoint(
                crearReporteDTO.ubicacionDTO().longitud(),
                crearReporteDTO.ubicacionDTO().latitud()
        );
        ;
        // Construir el reporte
        Reporte reporte = Reporte.builder()
                .nombre(crearReporteDTO.titulo())
                .descripcion(crearReporteDTO.descripcion())
                .categoria(crearReporteDTO.categoria().toUpperCase())
                .usuarioId(crearReporteDTO.idUsuario())
                .fechaCreacion(crearReporteDTO.fechaCreacion()) // Ya se recibe desde el DTO
                .estado(crearReporteDTO.estado())               // Ya se recibe desde el DTO
                .ubicacion(geoPoint)
                // .rutaImagenes(crearReporteDTO.rutaImagenes()) // Descomenta si manejas imágenes
                .build();

        return reporteRepo.save(reporte);
    }


    @Override
    public Page<Reporte> listarReportes(Pageable pageable) {
        return reporteRepo.findAll(pageable);
    }


    @Override
    public void editarReporte(String idReporte, EditarReporteDTO dto) throws Exception {
        Reporte reporte = obtenerReportePorId(idReporte);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuario = user.getUsername();

        if (!reporte.getUsuarioId().equals(idUsuario)) {
            throw new IllegalAccessException("No puedes modificar un reporte que no te pertenece.");
        }

        // Validar si puede ser modificado (si está RECHAZADO, dentro de 5 días)
        if (reporte.getEstado() == EstadoReporte.RECHAZADO && reporte.getFechaRechazo() != null) {
            LocalDateTime fechaLimite = reporte.getFechaRechazo().plusDays(5);
            if (LocalDateTime.now().isAfter(fechaLimite)) {
                throw new IllegalAccessException("El tiempo para modificar este reporte ha expirado.");
            }
        }

        // Editar valores
        reporte.setNombre(dto.getNombre());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setCategoria(dto.getCategoria().toUpperCase());

        // Si estaba rechazado, volver a pendiente
        if (reporte.getEstado() == EstadoReporte.RECHAZADO) {
            reporte.setEstado(EstadoReporte.PENDIENTE);
            reporte.setMotivoCambioEstado(null);
            reporte.setFechaRechazo(null);
        }

        reporteRepo.save(reporte);
    }





    @Override
    public Reporte obtenerReportePorId(String id) {
        Reporte reporte = reporteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        System.out.println("Reporte encontrado: " + reporte);
        return reporte;
    }


    @Override
    public List<InfoReporteDTO> obtenerReporte(String categoria, EstadoReporteDTO estadoReporteDTO) {
        return List.of();
    }

   // @Override
   // public List<InfoReporteDTO> obtenerReportesUbicacion(Ubicacion ubicacion) {
    //  return List.of();
    //}

    @Override
    public void eliminarReporte(String id) {
        reporteRepo.deleteById(id);
    }


    public boolean agregarComentario(String idReporte, ComentarioDTO comentarioDTO) {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        Comentario comentario = new Comentario(comentarioDTO.getIdUsuario(), comentarioDTO.getIdReporte(),
                comentarioDTO.getComentario(), comentarioDTO.getFecha());
        reporte.getComentarios().add(comentario);

        reporteRepo.save(reporte);
        return true;
    }

    @Override
    public boolean marcarImportante(String idReporte) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuario = user.getUsername();

        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        // Verificar si el usuario es el creador del reporte
        if (reporte.getUsuarioId().equals(idUsuario)) {
            return false; // No se permite que el creador lo marque como importante
        }

        // Verificar si ya lo marcó antes
        if (reporte.getUsuariosQueMarcaronImportante().contains(idUsuario)) {
            return false;
        }

        // Marcar como importante
        reporte.getUsuariosQueMarcaronImportante().add(idUsuario);
        reporte.setConteoImportante(reporte.getConteoImportante() + 1);
        reporteRepo.save(reporte);

        return true;
    }



    @Override
    public boolean cambiarEstado(String idReporte, EstadoReporteDTO estadoReporteDTO) {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        EstadoReporte nuevoEstado = EstadoReporte.valueOf(estadoReporteDTO.getNuevoEstado());
        reporte.setEstado(nuevoEstado);
        reporte.setMotivoCambioEstado(estadoReporteDTO.getMotivo());

        if (nuevoEstado == EstadoReporte.RECHAZADO) {
            reporte.setFechaRechazo(LocalDateTime.now());
        }

        reporteRepo.save(reporte);

        return true;
    }

    @Override
    public List<ReporteDTO> listarReportesPorCategoriaYFechas(String categoria, LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        try {
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoría no válida: " + categoria);
        }

        List<Reporte> reportes = reporteRepo.findByCategoriaAndFechaCreacionBetween(categoria, fechaInicio, fechaFin);
        return reportes.stream().map(reporteMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public byte[] generarInformePDF(String categoria, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoría no válida: " + categoria);
        }

        List<Reporte> reportes = reporteRepo.findByCategoriaAndFechaCreacionBetween(categoria, fechaInicio, fechaFin);
        if (reportes.isEmpty()) {
            throw new RuntimeException("No hay reportes disponibles para la categoría y fechas proporcionadas.");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDoc = new PdfDocument(writer)) {

            // Crear documento de iText 7 usando PdfDocument
            Document document = new Document(pdfDoc);
            document.setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA));

            // Agregar contenido al PDF
            document.add(new Paragraph("Informe de Reportes")
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Categoría: " + categoria));
            document.add(new Paragraph("Fecha de Inicio: " + fechaInicio));
            document.add(new Paragraph("Fecha de Fin: " + fechaFin));
            document.add(new Paragraph("\n"));

            for (Reporte reporte : reportes) {
                document.add(new Paragraph("ID: " + reporte.getId()));
                document.add(new Paragraph("Nombre: " + reporte.getNombre()));
                document.add(new Paragraph("Descripción: " + reporte.getDescripcion()));
                document.add(new Paragraph("Estado: " + reporte.getEstado()));
                document.add(new Paragraph("---------------------------"));
            }

            // Cerrar el documento
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return outputStream.toByteArray();
    }

    @Override
    public Page<Reporte> obtenerReportesCercanos(double longitud, double latitud, Pageable pageable) {
        return reporteRepo.obtenerCercanos(longitud, latitud, pageable);
    }

}
