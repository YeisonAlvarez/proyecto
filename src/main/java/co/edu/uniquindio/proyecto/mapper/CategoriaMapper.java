package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    Categoria toDocument(CategoriaDTO categoriaDTO);
    CategoriaDTO toDTO(Categoria categoria);

    // Métodos de conversión para ObjectId ↔ String
    default String map(ObjectId value) {
        return value != null ? value.toString() : null;
    }

    default ObjectId map(String value) {
        return value != null ? new ObjectId(value) : null;
    }
}
