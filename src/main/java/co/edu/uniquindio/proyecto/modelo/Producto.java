package co.edu.uniquindio.proyecto.modelo;


import co.edu.uniquindio.proyecto.modelo.enums.Categoria;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.util.List;


@Document("productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Producto {
    @Id
    @EqualsAndHashCode.Include
    private ObjectId id;
    private String nombre;
    private List<Categoria> tipoProducto;
    private int unidades;
    private float precio;
}