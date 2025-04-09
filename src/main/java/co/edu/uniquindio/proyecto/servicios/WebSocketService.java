package co.edu.uniquindio.proyecto.servicios;

public interface WebSocketService {

    void sendNotificationToUser(String userId, String message);

    void sendNotificationToAll(String message);
}
