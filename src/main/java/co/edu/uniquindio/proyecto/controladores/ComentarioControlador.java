package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.servicios.ComentarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioControlador {

    private final ComentarioServicio comentarioServicio;

    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crearComentario(@Valid @RequestBody CrearComentarioDTO crearComentarioDTO) throws Exception {
        comentarioServicio.crearComentario(crearComentarioDTO.idReporte(), crearComentarioDTO);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Comentario creado exitosamente", null));
    }

    @GetMapping("/{idReporte}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentarios(@PathVariable String idReporte) throws Exception {
        return ResponseEntity.ok(comentarioServicio.obtenerComentarios(idReporte));
    }
}