package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.enums.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReporteRepo extends MongoRepository<Reporte, String> {

    // Obtener reportes con paginación
    Page<Reporte> findAll(Pageable pageable);

    // Buscar reportes por categoría con paginación
    Page<Reporte> findByCategoria(Categoria categoria, Pageable pageable);

    // Buscar reportes de un usuario por su ID, ordenados por fecha
    List<Reporte> findByUsuarioIdOrderByFechaCreacionAsc(String usuarioId);
    List<Reporte> findByUsuarioIdOrderByFechaCreacionDesc(String usuarioId);

    // Buscar reportes cuyo nombre contenga un texto
    @Query("{'nombre': {$regex: ?0, $options: 'i'}}")
    Page<Reporte> buscarPorNombre(String texto, Pageable pageable);
}
