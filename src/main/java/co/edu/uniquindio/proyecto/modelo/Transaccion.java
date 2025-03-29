package co.edu.uniquindio.proyecto.modelo;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;


@Document("transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Transaccion {
    @Id
    @EqualsAndHashCode.Include
    private ObjectId id;
    private ObjectId idCliente;
    private List<DetalleProducto> productos;
    private LocalDateTime fecha;
}
