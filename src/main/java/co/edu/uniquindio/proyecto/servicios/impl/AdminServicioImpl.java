package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.EditarAdminDTO;
import co.edu.uniquindio.proyecto.excepciones.RecursoNoEncontradoException;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.AdminServicio;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServicioImpl implements AdminServicio {

    private final UsuarioRepo usuarioRepo;

    @Override
    public void editarCuenta(EditarAdminDTO dto) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuarioAutenticado = user.getUsername();

        ObjectId objectId = new ObjectId(idUsuarioAutenticado);
        Usuario admin = usuarioRepo.findById(objectId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un administrador con el ID: " + idUsuarioAutenticado));

        if (dto.telefono() != null) admin.setTelefono(dto.telefono());
        if (dto.direccion() != null) admin.setDireccion(dto.direccion());
        if (dto.ciudad() != null) admin.setCiudad(Ciudad.valueOf(dto.ciudad().toUpperCase()));

        usuarioRepo.save(admin);
    }

    @Override
    public void eliminarCuenta() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUsuarioAutenticado = user.getUsername();

        ObjectId objectId = new ObjectId(idUsuarioAutenticado);
        Usuario admin = usuarioRepo.findById(objectId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un administrador con el ID: " + idUsuarioAutenticado));

        admin.setEstado(EstadoUsuario.ELIMINADO);
        usuarioRepo.save(admin);
    }
}
