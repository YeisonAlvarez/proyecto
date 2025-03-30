package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.CategoriaEnum;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Data
@Document("categorias")
public class Categoria {

    @Id
    private String id; //  MongoDB lo maneja como ObjectId internamente

    private CategoriaEnum nombre;
    private String descripcion;

    @Builder
    public Categoria( CategoriaEnum nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

}