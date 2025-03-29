package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;

import java.util.List;

public interface UsuarioServicio {

    void crear(CrearUsuarioDTO cuenta) throws Exception;
    void eliminar(String id) throws Exception;
    void editar(EditarUsuarioDTO cuenta) throws Exception;
    UsuarioDTO obtener(String id) throws Exception;
    List<UsuarioDTO> listarTodos(String nombre, String ciudad, int pagina);
    boolean activarCuenta(String email, String codigo);
    boolean recuperarContraseña(RecuperarPasswordDTO recuperarPasswordDTO);

}
