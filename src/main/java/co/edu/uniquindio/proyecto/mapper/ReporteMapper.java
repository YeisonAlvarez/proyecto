package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.ReporteDTO;
import co.edu.uniquindio.proyecto.dto.CrearReporteDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    // Convierte el DTO de creación a documento Reporte
    @Mapping(target = "categoria", expression = "java(Categoria.valueOf(crearReporteDTO.categoria().toUpperCase()))")
    @Mapping(target = "fechaCreacion", expression = "java(java.time.LocalDateTime.now())")
    Reporte toDocument(CrearReporteDTO crearReporteDTO);

    // Convierte el documento Reporte a ReporteDTO
    ReporteDTO toDTO(Reporte reporte);

    // Actualiza el documento Reporte con los datos de EditarReporteDTO
    void toDocument(EditarReporteDTO editarReporteDTO, @MappingTarget Reporte reporte);
}
