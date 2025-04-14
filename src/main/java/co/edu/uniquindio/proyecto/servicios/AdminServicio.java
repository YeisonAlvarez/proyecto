package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.EditarAdminDTO;

public interface AdminServicio {
    void editarCuenta(EditarAdminDTO dto) throws Exception;
    void eliminarCuenta() throws Exception;
}



