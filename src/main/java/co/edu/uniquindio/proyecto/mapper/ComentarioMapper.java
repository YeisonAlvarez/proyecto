package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    Comentario toDocument(CrearComentarioDTO crearComentarioDTO);
    ComentarioDTO toDTO(Comentario comentario);
}
