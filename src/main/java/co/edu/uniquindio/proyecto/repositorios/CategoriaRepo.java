package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CategoriaRepo extends  MongoRepository<Categoria, String>  {
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsById(String id);
    void deleteById(String id);
    Optional<Categoria> findById(String id);
}

