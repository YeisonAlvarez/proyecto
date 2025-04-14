package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.EditarAdminDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.servicios.AdminServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminControlador {

    private final AdminServicio adminServicio;

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/editar-cuenta")
    public ResponseEntity<MensajeDTO<String>> editarCuenta (
            @RequestBody EditarAdminDTO dto) throws Exception {

        adminServicio.editarCuenta(dto);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta actualizada correctamente", null));
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta() throws Exception {
        adminServicio.eliminarCuenta();
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada correctamente", null));
    }
}
