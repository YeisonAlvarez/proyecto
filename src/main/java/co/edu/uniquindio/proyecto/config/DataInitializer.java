package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Verifica si el admin ya existe
        if (usuarioRepo.findByEmail("admin@reportes.com").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador General")
                    .email("admin@reportes.com")
                    .password(passwordEncoder.encode("123456789"))
                    .rol(Rol.ADMINISTRADOR)
                    .estado(EstadoUsuario.ACTIVO)
                    .build();

            usuarioRepo.save(admin);
            System.out.println("Administrador creado exitosamente");
        } else {
            System.out.println("Administrador ya existe");
        }
    }
}
