package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.excepciones.*;
import co.edu.uniquindio.proyecto.mapper.ReporteMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;  // Para iText
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServicioImpl implements ReporteServicio {

    private final ReporteRepo reporteRepo;
    private final ReporteMapper reporteMapper;
    private final WebSocketNotificationService webSocketNotificationService;
    private final EmailServicio emailServicio;
    private final CategoriaRepo categoriaRepo;
    private  final UsuarioServicio usuarioServicio;


    @Override
    public Reporte crearReporte(CrearReporteDTO crearReporteDTO) throws Exception {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuarioAutenticado = user.getUsername();

        if (!crearReporteDTO.idUsuario().equals(idUsuarioAutenticado)) {
            throw new OperacionNoPermitidaException("El id del usuario autenticado no coincide con el creador del reporte.");
        }

        Categoria categoria = categoriaRepo.findByNombreIgnoreCase(crearReporteDTO.categoria())
                .orElseThrow(() -> new RecursoNoEncontradoException("La categoría '" + crearReporteDTO.categoria() + "' no existe."));


        GeoJsonPoint geoPoint = new GeoJsonPoint(
                crearReporteDTO.ubicacionDTO().longitud(),
                crearReporteDTO.ubicacionDTO().latitud()
        );

        Reporte reporte = Reporte.builder()
                .nombre(crearReporteDTO.titulo())
                .descripcion(crearReporteDTO.descripcion())
                .categoria(categoria.getNombre()) // Guardamos el nombre tal como está en la BD
                .usuarioId(idUsuarioAutenticado)
                .fechaCreacion(crearReporteDTO.fechaCreacion())
                .estado(crearReporteDTO.estado())
                .ubicacion(geoPoint)
                // .rutaImagenes(crearReporteDTO.rutaImagenes()) // Descomenta si manejas imágenes
                .build();

        List<UsuarioNotificacionDTO> usuariosConEmailActivo = usuarioServicio.listarUsuariosConNotificacionEmailActiva();
        List<UsuarioNotificacionDTO> usuariosConAppActivo = usuarioServicio.listarUsuariosConNotificacionAppActiva();

        //Notificar via app todos los usuarios con notificacion via app activa
        for (UsuarioNotificacionDTO usuario : usuariosConAppActivo) {

            webSocketNotificationService.notificarClientes( new NotificacionDTO(
                    "Nuevo reporte en tu zona",
                    "Se ha reportado un nuevo incidente cerca de tu ubicación. Revisa los detalles para mantenerte informado.",
                    "/topic/reports"
            ) );
        }

        //Notificar via app todos los usuarios con notificacion via email activa
        for (UsuarioNotificacionDTO usuario : usuariosConEmailActivo) {
            EmailDTO email = new EmailDTO(
                    usuario.email(),
                    "Nuevo reporte en tu zona",
                    "¡Hola! Se ha registrado un nuevo reporte cerca de tu ubicación. Ingresa a la plataforma para ver más detalles."
            );

                emailServicio.enviarCorreo(email);
        }

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
            throw new OperacionNoPermitidaException("No puedes modificar un reporte que no te pertenece.");
        }

        if (reporte.getEstado() == EstadoReporte.RECHAZADO && reporte.getFechaRechazo() != null) {
            LocalDateTime fechaLimite = reporte.getFechaRechazo().plusDays(5);
            if (LocalDateTime.now().isAfter(fechaLimite)) {
                throw new OperacionNoPermitidaException("El tiempo para modificar este reporte ha expirado.");
            }
        }

        // Validar que la nueva categoría exista
        String categoriaMayuscula = dto.getCategoria().toUpperCase();
        boolean existeCategoria = categoriaRepo.existsByNombreIgnoreCase(categoriaMayuscula);
        if (!existeCategoria) {
            throw new IllegalArgumentException("La categoría '" + dto.getCategoria() + "' no existe.");
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
    public Reporte obtenerReportePorId(String id) throws Exception {
        Reporte reporte = reporteRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reporte con ID '" + id + "' no encontrado"));
        return reporte;
    }


    @Override
    public void eliminarReporte(String id) throws Exception {
        Reporte reporte = reporteRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede eliminar. Reporte con ID '" + id + "' no encontrado."));

        reporte.setEstado(EstadoReporte.ELIMINADO);
        reporteRepo.save(reporte);
    }



    @Override
    public boolean marcarImportante(String idReporte) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuario = user.getUsername();

        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reporte con ID '" + idReporte + "' no encontrado"));

        if (reporte.getUsuarioId().equals(idUsuario)) {
            throw new OperacionNoPermitidaException("No puedes marcar como importante tu propio reporte.");
        }

        if (reporte.getUsuariosQueMarcaronImportante().contains(idUsuario)) {
            throw new OperacionNoPermitidaException("Ya has marcado este reporte como importante.");
        }

        // Marcar como importante
        reporte.getUsuariosQueMarcaronImportante().add(idUsuario);
        reporte.setConteoImportante(reporte.getConteoImportante() + 1);
        reporteRepo.save(reporte);

        return true;
    }


    @Override
    public boolean cambiarEstado(String idReporte, EstadoReporteDTO estadoReporteDTO) throws Exception {
        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reporte con ID '" + idReporte + "' no encontrado"));

        EstadoReporte nuevoEstado = EstadoReporte.valueOf(estadoReporteDTO.getNuevoEstado());

        // Obtener info de usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String idUsuario = user.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean esAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        boolean esCliente = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));

        if (esCliente) {
            // Cliente solo puede cambiar su propio reporte a RESUELTO
            if (!reporte.getUsuarioId().equals(idUsuario)) {
                throw new AccesoNoAutorizadoException("No puedes cambiar el estado de reportes que no te pertenecen.");
            }

            if (nuevoEstado != EstadoReporte.RESUELTO) {
                throw new AccesoNoAutorizadoException("Como cliente solo puedes marcar tu reporte como RESUELTO.");
            }

            // Si el reporte fue rechazado, verificar si está dentro de los 5 días permitidos
            if (reporte.getEstado() == EstadoReporte.RECHAZADO && reporte.getFechaRechazo() != null) {
                LocalDateTime limite = reporte.getFechaRechazo().plusDays(5);
                if (LocalDateTime.now().isAfter(limite)) {
                    throw new IllegalStateException("No puedes reenviar un reporte rechazado después de 5 días.");
                }
            }
        }

        if (esAdmin) {
            if (nuevoEstado == EstadoReporte.RECHAZADO) {
                if (estadoReporteDTO.getMotivo() == null || estadoReporteDTO.getMotivo().isBlank()) {
                    throw new IllegalArgumentException("Debe proporcionar un motivo al rechazar el reporte.");
                }
                reporte.setFechaRechazo(LocalDateTime.now());
            }
        }

        // Actualizar estado y motivo
        reporte.setEstado(nuevoEstado);
        reporte.setMotivoCambioEstado(estadoReporteDTO.getMotivo());

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
    public byte[] generarInformePDF(String categoria, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws Exception {
        try {

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoría no válida: " + categoria);
        }

        List<Reporte> reportes = reporteRepo.findByCategoriaAndFechaCreacionBetween(categoria, fechaInicio, fechaFin);
        if (reportes.isEmpty()) {
            throw new RecursoNoEncontradoException("No hay reportes disponibles para la categoría y fechas proporcionadas.");
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


    @Override
    public List<ReporteDTO> buscarReportes(BuscarReporteDTO filtros) throws Exception {

        if (filtros == null) {
            throw new ValidacionException("Debe proporcionar un objeto de filtros para realizar la búsqueda.");
        }

        boolean hayFiltros = filtros.txtConsulta() != null ||
                filtros.categoria() != null ||
                filtros.estado() != null ||
                filtros.fechaInicio() != null ||
                filtros.fechaFin() != null ||
                (filtros.latitud() != null && filtros.longitud() != null);

        if (!hayFiltros) {
            throw new ValidacionException("Debe especificar al menos un filtro para buscar reportes.");
        }

        return reporteRepo.findAll().stream()
                .filter(reporte -> reporte.getEstado() != EstadoReporte.ELIMINADO) // excluir eliminados
                .filter(reporte -> {
                    boolean coincide = true;

                    if (filtros.txtConsulta() != null && !filtros.txtConsulta().isBlank()) {
                        String texto = filtros.txtConsulta().toLowerCase();
                        coincide = coincide && (
                                reporte.getNombre().toLowerCase().contains(texto) ||
                                        reporte.getDescripcion().toLowerCase().contains(texto)
                        );
                    }

                    if (filtros.categoria() != null && !filtros.categoria().isBlank()) {
                        coincide = coincide && reporte.getCategoria().equalsIgnoreCase(filtros.categoria());
                    }

                    if (filtros.estado() != null && !filtros.estado().isBlank()) {
                        coincide = coincide && reporte.getEstado().name().equalsIgnoreCase(filtros.estado());
                    }

                    if (filtros.fechaInicio() != null) {
                        coincide = coincide && !reporte.getFechaCreacion().toLocalDate().isBefore(filtros.fechaInicio());
                    }

                    if (filtros.fechaFin() != null) {
                        coincide = coincide && !reporte.getFechaCreacion().toLocalDate().isAfter(filtros.fechaFin());
                    }

                    if (filtros.latitud() != null && filtros.longitud() != null) {
                        coincide = coincide && reporte.getUbicacion().getY() == filtros.latitud() &&
                                reporte.getUbicacion().getX() == filtros.longitud();
                    }

                    return coincide;
                })
                .map(reporteMapper::toDTO)
                .toList();
    }



    @Override
    public void calificarReporte(String idReporte, int estrellas) throws Exception {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuario = user.getUsername();

        Reporte reporte = reporteRepo.findById(idReporte)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el reporte con ID: " + idReporte));

        if (reporte.getUsuarioId().equals(idUsuario)) {
            throw new ParametroInvalidoException("No puedes calificar tu propio reporte.");
        }

        if (estrellas < 1 || estrellas > 5) {
            throw new ValidacionException("La calificación debe estar entre 1 y 5 estrellas.");
        }

        // Asegurar que el mapa esté inicializado
        if (reporte.getCalificaciones() == null) {
            reporte.setCalificaciones(new HashMap<>());
        }

        // Registrar o actualizar la calificación (el put actualiza la calificacion del cliente si ya esxite)
        reporte.getCalificaciones().put(idUsuario, estrellas);

        double promedio = reporte.getCalificaciones()
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        reporte.setPromedioEstrellas(promedio);
        reporteRepo.save(reporte);
    }

    @Override
    public List<ReporteDTO> obtenerReportesPorUsuario() throws Exception{

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuario = user.getUsername();

        List<Reporte> reportes = reporteRepo.findByUsuarioId(idUsuario);

        if (reportes.isEmpty()) {
            throw new RecursoNoEncontradoException("El usuario no tiene reportes registrados.");
        }

        return reportes.stream()
                .map(reporteMapper::toDTO)
                .collect(Collectors.toList());
    }

}
