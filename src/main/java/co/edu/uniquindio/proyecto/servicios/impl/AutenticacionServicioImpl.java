package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.LoginDTO;
import co.edu.uniquindio.proyecto.dto.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.AutenticacionServicio;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutenticacionServicioImpl  implements AutenticacionServicio {


    private final UsuarioRepo usuarioRepo;

    @Override
    public boolean login(LoginDTO loginDTO) throws Exception {
        if (!validarCredenciales(loginDTO.email(), loginDTO.password())) {
            throw new Exception("Credenciales inválidas");
        }

        Usuario usuario = usuarioRepo.findByEmail(loginDTO.email())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new Exception("El usuario no está activado. Revisa tu correo para activarlo.");
        }

        return true;
    }

    @Override
    public boolean validarCredenciales(String email, String password) {
        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            return usuario.getPassword().equals(password) && usuario.getEstado().equals(EstadoUsuario.ACTIVO);
        }

        return false;
    }
}


