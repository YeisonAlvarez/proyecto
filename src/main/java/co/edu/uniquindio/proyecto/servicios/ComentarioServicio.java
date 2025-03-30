package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;

import java.util.List;

public interface ComentarioServicio {

    List<ComentarioDTO> obtenerComentarios(String idReporte) throws Exception;
    void crearComentario(String idReporte, CrearComentarioDTO crearComentarioDTO) throws Exception;
}
