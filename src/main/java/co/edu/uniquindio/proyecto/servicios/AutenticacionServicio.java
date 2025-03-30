package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.LoginDTO;
import co.edu.uniquindio.proyecto.dto.TokenDTO;

public interface AutenticacionServicio {

   boolean login (LoginDTO loginDTO)  throws Exception ;
   boolean validarCredenciales(String email, String password);

}
