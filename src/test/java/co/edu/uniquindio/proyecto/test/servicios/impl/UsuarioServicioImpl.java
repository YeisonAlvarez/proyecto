package co.edu.uniquindio.proyecto.test.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.excepciones.ElementoRepetidoException;
import co.edu.uniquindio.proyecto.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepo usuarioRepositorio;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServicioImpl(UsuarioRepo usuarioRepositorio, UsuarioMapper usuarioMapper) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public void crear(CrearUsuarioDTO cuenta) throws Exception {
        Usuario usuario = UsuarioMapper.toDocument(cuenta);
        usuarioRepositorio.save(usuario);
    }

    @Override
    public void eliminar(String id) throws Exception {

    }

    @Override
    public void editarCuenta(String id, EditarUsuarioDTO cuenta) throws Exception {

    }

    @Override
    public UsuarioDTO obtener(String id) throws Exception {
        return null;
    }

    @Override
    public List<UsuarioDTO> listarTodos(String nombre, String ciudad, int pagina) {
        return List.of();
    }

    @Override
    public boolean activarCuenta(String email, String codigo) {
        return false;
    }

    @Override
    public boolean cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        return false;
    }

    @Override
    public GenerarTokenDTO generarToken(String email) {
        return null;
    }

    @Override
    public void reenviarToken(String email) throws Exception {

    }

    @Override
    public void actualizarSuscripcionNotificaciones(String idUsuario, SuscripcionNotificacionesDTO suscripcion) throws Exception {

    }
}
