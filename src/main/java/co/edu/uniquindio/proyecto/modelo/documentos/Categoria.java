package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
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

    private String nombre;
    private String descripcion;

    @Builder
    public Categoria( String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

}