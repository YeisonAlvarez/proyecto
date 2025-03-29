package co.edu.uniquindio.proyecto.modelo;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.util.List;


@Document("clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Cliente {
    @Id
    @EqualsAndHashCode.Include
    private ObjectId id;
    private String cedula;
    private String nombre;
    private String email;
    private List<String> telefonos;
    //private String telefono;
}

