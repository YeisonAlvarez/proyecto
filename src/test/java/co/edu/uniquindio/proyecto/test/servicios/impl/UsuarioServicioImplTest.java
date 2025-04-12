package co.edu.uniquindio.proyecto.test.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.excepciones.ElementoNoEncontradoException;
import co.edu.uniquindio.proyecto.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;


class UsuarioServicioImplTest {

    @InjectMocks
    private UsuarioServicioImpl usuarioServicio;

    @Mock
    private UsuarioRepo UsuarioRepo;

    @Mock
    private UsuarioMapper usuarioMapper;

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Inicializa mocks de Mockito
        MockitoAnnotations.openMocks(this);

        // Inicializa validador de Bean Validation
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    //Test de crear
    @Test
    void crearUsuarioExitosamente() throws Exception {
        // Preparación de datos
        CrearUsuarioDTO crearUsuarioDTO = new CrearUsuarioDTO(
                "Juan Pérez",             // nombre
                "3212212",                // teléfono
                Ciudad.ARMENIA,           // ciudad (enum)
                "Calle 10 #5-20",         // dirección (String)
                "juan@example.com",       // email
                "12345678"                // password
        );
        Usuario usuario = new Usuario();
        usuario.setEmail(crearUsuarioDTO.email());
//        // Configuración de mocks
        when(UsuarioRepo.findByEmail(crearUsuarioDTO.email())).thenReturn(Optional.empty());
        when(UsuarioMapper.toDocument(crearUsuarioDTO)).thenReturn(usuario);
        // Ejecución
        usuarioServicio.crear(crearUsuarioDTO);
        // Verificación
        verify(UsuarioRepo).save(usuario);
    }
}
