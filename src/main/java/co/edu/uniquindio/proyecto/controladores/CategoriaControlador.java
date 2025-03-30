package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.servicios.CategoriaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categorias")
public class CategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crear(@Valid @RequestBody CategoriaDTO categoria) throws Exception {
        categoriaServicio.crearCategoria(categoria);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Categoría creada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> actualizar(@PathVariable String id, @RequestBody CategoriaDTO categoria) throws Exception {
        categoriaServicio.actualizarCategoria(categoria);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría actualizada exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String id) throws Exception {
        categoriaServicio.eliminarCategoria(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría eliminada exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<CategoriaDTO>> obtener(@PathVariable String id) throws Exception {
        CategoriaDTO info = categoriaServicio.obtenerCategoria(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, info));
    }

    @GetMapping
    public ResponseEntity<MensajeDTO<List<CategoriaDTO>>> listar() throws Exception {
        List<CategoriaDTO> lista = categoriaServicio.listarCategorias();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
}

