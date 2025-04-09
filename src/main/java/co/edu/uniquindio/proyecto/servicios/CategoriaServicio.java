package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaServicio {

    void crearCategoria(CategoriaDTO categoria) throws Exception ;
    void actualizarCategoria(String id, CategoriaDTO categoria) throws Exception;
    void eliminarCategoria(String id) throws Exception;
    CategoriaDTO obtenerCategoria(String id) throws Exception;
    List <CategoriaDTO> listarCategorias() throws Exception;
}
