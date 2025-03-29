package co.edu.uniquindio.proyecto.test;


import co.edu.uniquindio.proyecto.repositorios.ClienteRepo;
import org.junit.jupiter.api.Test;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import co.edu.uniquindio.proyecto.modelo.Cliente;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ClienteTest {


    @Autowired
    private ClienteRepo clienteRepo;

    @Test
    public void registrarTest(){
        //Creamos un cliente con sus propiedades
        Cliente cliente = Cliente.builder()
                .cedula("123456")
                .nombre("Pepito")
                .email("pepito@email.com")
                .telefonos( List.of("telefono1", "telefono2") ).build();


        //Guardamos el cliente en la base de datos
        Cliente clienteCreado = clienteRepo.save(cliente);


        //Verificamos que se haya guardado validando que no sea null
        assertNotNull(clienteCreado);
    }


}
