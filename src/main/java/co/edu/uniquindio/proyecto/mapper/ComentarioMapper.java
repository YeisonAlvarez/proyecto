package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    Comentario crearDtoToComentario(CrearComentarioDTO dto);

    @Mapping(target = "usuarioId", source = "idUsuario")
    @Mapping(target = "reporteId", source = "idReporte")
    void completarDatos(@MappingTarget Comentario comentario, String idUsuario, String idReporte);
}




