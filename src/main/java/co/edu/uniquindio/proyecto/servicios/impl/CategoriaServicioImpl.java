package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.excepciones.ElementoNoEncontradoException;
import co.edu.uniquindio.proyecto.excepciones.ElementoRepetidoException;
import co.edu.uniquindio.proyecto.mapper.CategoriaMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.servicios.CategoriaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaServicioImpl implements CategoriaServicio {

    private final CategoriaRepo categoriaRepo;
    private final CategoriaMapper categoriaMapper;

    @Override
    public void crearCategoria(CategoriaDTO categoriaDTO) throws Exception {
        if (categoriaRepo.existsByNombre(String.valueOf(categoriaDTO.nombre()))) {
            throw new ElementoRepetidoException("La categoría ya existe");
        }

        Categoria categoria = categoriaMapper.toDocument(categoriaDTO);
        categoriaRepo.save(categoria);
    }

    @Override
    public void actualizarCategoria(String id, CategoriaDTO categoriaDTO) throws Exception {
        // Verificar si la categoría con el ID existe
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException("Categoría no encontrada"));

        // Validar que el nombre de la categoría esté en el Enum
        try {
            String.valueOf(categoriaDTO.nombre()); // Lanza IllegalArgumentException si el nombre no está en el Enum
        } catch (IllegalArgumentException e) {
            throw new Exception("El nombre de la categoría debe ser uno de los valores del Enum");
        }

        // Actualizar la información de la categoría
        categoria.setNombre(String.valueOf(categoriaDTO.nombre())); // Convertimos el nombre a Enum
        categoria.setDescripcion(categoriaDTO.descripcion());

        // Guardar la categoría actualizada en la base de datos
        categoriaRepo.save(categoria);
    }


    @Override
    public void eliminarCategoria(String id) throws Exception {
        if (!categoriaRepo.existsById(id)) {
            throw new ElementoNoEncontradoException("Categoría no encontrada");
        }
        categoriaRepo.deleteById(id);
    }


    //cualquier usuario
    @Override
    public CategoriaDTO obtenerCategoria(String id) throws Exception {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la categoría no puede estar vacío");
        }

        Optional<Categoria> optionalCategoria = categoriaRepo.findById(id);

        if (!optionalCategoria.isPresent()) {
            throw new ElementoNoEncontradoException("Categoría no encontrada");
        }

        return categoriaMapper.toDTO(optionalCategoria.get());
    }


    //cualquier usuario
    @Override
    public List<CategoriaDTO> listarCategorias() throws Exception {
        List<Categoria> categorias = categoriaRepo.findAll();

        if (categorias == null || categorias.isEmpty()) {
            throw new ElementoNoEncontradoException("No hay categorías registradas.");
        }

        List<CategoriaDTO> listaCategorias = new ArrayList<>();
        for (Categoria categoria : categorias) {
            listaCategorias.add(categoriaMapper.toDTO(categoria));
        }

        return listaCategorias;
    }
}
