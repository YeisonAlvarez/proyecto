package co.edu.uniquindio.proyecto.dto;

public class MensajeDTO<T> {

    private boolean error;
    private T mensaje;

    public MensajeDTO(boolean error, T mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getMensaje() {
        return mensaje;
    }

    public void setMensaje(T mensaje) {
        this.mensaje = mensaje;
    }
}
