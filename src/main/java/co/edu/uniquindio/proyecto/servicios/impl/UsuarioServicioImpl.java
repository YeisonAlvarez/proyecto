package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.excepciones.ElementoNoEncontradoException;
import co.edu.uniquindio.proyecto.excepciones.ElementoRepetidoException;
import co.edu.uniquindio.proyecto.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepo usuarioRepo;
    private final UsuarioMapper usuarioMapper;
    private final MongoTemplate mongoTemplate;
    private final EmailServicio emailServicio;



    private boolean existeEmail(String email) {
        return usuarioRepo.findByEmail(email).isPresent();
    }

    @Override
    public void crear(CrearUsuarioDTO crearUsuarioDTO) throws Exception {

        // Validar que el email no esté repetido
        if (existeEmail(crearUsuarioDTO.email())) {
            throw new ElementoRepetidoException("El email ya está registrado");
        }
        // Mapear el DTO a documento
        Usuario usuario = usuarioMapper.toDocument(crearUsuarioDTO);
        //Configurar datos adicionales antes de guardar
        usuario.setEstado(EstadoUsuario.INACTIVO); //  Usuario inactivo hasta que se active
        usuario.setFechaRegistro(LocalDateTime.now());
        TokenDTO token =generarToken( crearUsuarioDTO);
        if (token == null || token.token().isBlank()) {
            throw new RuntimeException("Error en la generación del token");
        }
        usuario.setCodigoActivacion(token.token());
        //Guardar usuario en MongoDB
        usuarioRepo.save(usuario);

    }


    @Override
    public void eliminar(String id) throws ElementoNoEncontradoException {

        // Obtener el usuario que se quiere eliminar
        Usuario usuario = obtenerPorId(id);

        // Modificar el estado del usuario
        usuario.setEstado(EstadoUsuario.ELIMINADO);

        //Como el objeto cuenta ya tiene un id, el save() no crea un nuevo registro sino que actualiza el que ya existe
        usuarioRepo.save(usuario);
    }

    @Override
    public void editar(EditarUsuarioDTO editarUsuarioDTO) throws ElementoNoEncontradoException {
        // Obtener el usuario que se quiere modificar
        Usuario cuentaModificada = obtenerPorId(editarUsuarioDTO.id());
        // Mapear los datos actualizados al usuario existente
        usuarioMapper.toDocument(editarUsuarioDTO, cuentaModificada);
        //Como el objeto cuenta ya tiene un id, el save() no crea un nuevo registro sino que actualiza el que ya existe
        usuarioRepo.save(cuentaModificada);
    }


    @Override
    public UsuarioDTO obtener(String id) throws ElementoNoEncontradoException {
        Usuario usuario = obtenerPorId(id);
        return usuarioMapper.toDTO(usuario);
    }


    @Override
    public List<UsuarioDTO> listarTodos(String nombre, String ciudad, int pagina) {
        if (pagina < 0) throw new RuntimeException("La página no puede ser menor a 0");

        Query query = new Query().with(PageRequest.of(pagina, 5));

        if (nombre != null && !nombre.isEmpty()) {
            query.addCriteria(Criteria.where("nombre").regex("^" + nombre, "i"));
        }

        if (ciudad != null && !ciudad.isEmpty()) {
            query.addCriteria(Criteria.where("ciudad").is(ciudad));
        }

        List<Usuario> usuarios = mongoTemplate.find(query, Usuario.class);
        return usuarios.stream().map(usuarioMapper::toDTO).toList();
    }


    private Usuario obtenerPorId(String id) throws ElementoNoEncontradoException {
        // Buscamos el usuario que se quiere obtener
        if (!ObjectId.isValid(id)) {
            throw new ElementoNoEncontradoException("No se encontró el usuario con el id "+id);
        }

        // Convertimos el id de String a ObjectId
        ObjectId objectId = new ObjectId(id);
        Optional<Usuario> optionalCuenta = usuarioRepo.findById(objectId);

        // Si no se encontró el usuario, lanzamos una excepción
        if(optionalCuenta.isEmpty()){
            throw new ElementoNoEncontradoException("No se encontró el usuario con el id "+id);
        }

        return optionalCuenta.get();
    }

    @Override
    public boolean activarCuenta(String email, String codigo) {
        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            if (usuario.getCodigoActivacion().equals(codigo)) {
                usuario.setEstado(EstadoUsuario.ACTIVO);
                usuario.setCodigoActivacion(null); // Eliminar el código usado
                usuarioRepo.save(usuario);
                return true;
            }
        }

        return false;
    }

    @Autowired
    private UsuarioRepo usuarioRepositorio;

    public boolean recuperarContraseña(RecuperarPasswordDTO recuperarPasswordDTO) {
        // Asegúrate de que el repositorio esté siendo utilizado correctamente
        Usuario usuario = usuarioRepositorio.findByEmail(recuperarPasswordDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return true;
    }

    @Override
    public TokenDTO generarToken(CrearUsuarioDTO usuarioDTO) throws Exception {

        SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        // Generar el token JWT
        Instant ahora = Instant.now();
        Instant expiracion = ahora.plusSeconds(900); // 15 minutos de validez

        String token = Jwts.builder()
                .setSubject(usuarioDTO.email())
                .claim("rol", Rol.CLIENTE)
                .setIssuedAt(Date.from(ahora))
                .setExpiration(Date.from(expiracion))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        TokenDTO tokenDTO = new TokenDTO(token, "Bearer", expiracion);

        // Validar que el token se generó correctamente
        if (tokenDTO.token().isBlank()) {
            throw new RuntimeException("Error generando el token");
        }

        // Crear el correo con el token
        EmailDTO emailDTO = new EmailDTO(
                usuarioDTO.email(),
                "Token de Autenticación - Alertas Urbanas",
                "Hola, aquí está tu token de autenticación:\n\n" + tokenDTO.token()
        );

        // Enviar el correo
        emailServicio.enviarCorreo(emailDTO);

        return tokenDTO;
    }


}