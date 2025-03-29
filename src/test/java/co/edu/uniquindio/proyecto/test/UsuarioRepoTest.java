package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UsuarioRepoTest {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    void testBuscarPorEmailYPassword() {
        Usuario usuario = usuarioRepo.findByEmailAndPassword("correo@ejemplo.com", "password123");
        assertNotNull(usuario);
    }

    @Test
    void testBuscarUsuariosPorRol() {
        List<Usuario> usuarios = usuarioRepo.findByRol(Rol.ADMIN);
        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void testBuscarUsuariosPorEstado() {
        List<Usuario> usuarios = usuarioRepo.findByEstado(EstadoUsuario.ACTIVO);
        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void testBuscarUsuariosPorCiudad() {
        List<Usuario> usuarios = usuarioRepo.findByCiudad(Ciudad.BOGOTA);
        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void testBuscarUsuariosPorNombreConteniendoTexto() {
        Page<Usuario> usuarios = usuarioRepo.findByNombreContainingIgnoreCase("Juan", PageRequest.of(0, 5));
        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
    }
}
