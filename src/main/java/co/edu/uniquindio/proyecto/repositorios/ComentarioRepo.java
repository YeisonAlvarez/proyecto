package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ComentarioRepo extends MongoRepository<Comentario, String> {

    // Obtener comentarios por reporte
    List<Comentario> findByIdReporte(String idReporte);

    // Buscar comentarios de un usuario
    List<Comentario> findByIdUsuario(String idUsuario);

    // Buscar comentarios que contengan un texto
    @Query("{'comentario': {$regex: ?0, $options: 'i'}}")
    List<Comentario> buscarPorTexto(String texto);
}