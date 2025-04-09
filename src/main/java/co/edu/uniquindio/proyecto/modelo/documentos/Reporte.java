package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor



@Data
@Builder
@Document("reportes")
public class Reporte {

    @Id
    private String id; // MongoDB maneja este campo como ObjectId internamente

    private String nombre;
    private String descripcion;
    private String categoria;  //Revisar si queda asi o con categoriaId
    private String usuarioId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRechazo;
    private int conteoImportante;
    private Set<String> usuariosQueMarcaronImportante = new HashSet<>();
    private EstadoReporte estado;
    private String motivoCambioEstado;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint ubicacion;

    private List<Comentario> comentarios = new ArrayList<>();

    @Field
    private List<Integer> calificaciones = new ArrayList<>();

    @Field
    private double promedioEstrellas;


}