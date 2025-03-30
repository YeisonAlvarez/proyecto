package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.*;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crear(@Valid @RequestBody CrearUsuarioDTO cuenta) throws Exception{
        usuarioServicio.crear(cuenta);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Pre-registro exitoso. Revisa el token enviado a tu correo para activar tu cuenta."));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<UsuarioDTO>> obtener(@PathVariable String id) throws Exception{
        UsuarioDTO info = usuarioServicio.obtener(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, info));
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String id) throws Exception{
        usuarioServicio.eliminar(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<MensajeDTO<List<UsuarioDTO>>> listarTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam int pagina
    ){
        List<UsuarioDTO> lista = usuarioServicio.listarTodos(nombre, ciudad, 1);
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@Valid @RequestBody EditarUsuarioDTO cuenta) throws Exception{
        usuarioServicio.editar(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }

    @PutMapping("/activar/{email}/{codigo}")
    public ResponseEntity<MensajeDTO<String>> activarCuenta(@PathVariable String email, @PathVariable String codigo) {
        boolean activado = usuarioServicio.activarCuenta(email, codigo);

        if (activado) {
            return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta activada correctamente"));
        } else {
            return ResponseEntity.status(400).body(new MensajeDTO<>(true, "Código de activación incorrecto"));
        }
    }

    @PostMapping("/auth/recover")
    public ResponseEntity<MensajeDTO<String>> recuperarContraseña(@RequestBody RecuperarPasswordDTO recuperarPasswordDTO) {
        boolean exito = usuarioServicio.recuperarContraseña(recuperarPasswordDTO);

        if (exito) {
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo de recuperación enviado"));
        } else {
            return ResponseEntity.status(400).body(new MensajeDTO<>(true, "No se pudo enviar el correo de recuperación"));
        }
    }


}