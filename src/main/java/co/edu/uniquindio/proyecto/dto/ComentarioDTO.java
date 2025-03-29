package co.edu.uniquindio.proyecto.dto;

public class ComentarioDTO {

    private String idUsuario;   // ID del usuario que realiza el comentario
    private String comentario;  // El texto del comentario

    // Constructor
    public ComentarioDTO(String idUsuario, String comentario) {
        this.idUsuario = idUsuario;
        this.comentario = comentario;
    }

    // Getters y Setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
