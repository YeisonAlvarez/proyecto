package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.LoginDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.TokenDTO;
import co.edu.uniquindio.proyecto.servicios.AutenticacionServicio;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.uniquindio.proyecto.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;


    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<String>> iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        try {
            autenticacionServicio.login(loginDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Inicio de sesión exitoso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MensajeDTO<>(true, e.getMessage()));
        }
    }


}