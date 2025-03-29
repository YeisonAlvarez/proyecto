package co.edu.uniquindio.proyecto.modelo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;


@Document("pagos")
@AllArgsConstructor
@Getter
@Setter
public class Pago {

    @Id
    private ObjectId id;


    private LocalDateTime fecha;
    private float totalPagado;
    private String estado;
    private String metodoPago;
    private ObjectId idTransaccion;


}
