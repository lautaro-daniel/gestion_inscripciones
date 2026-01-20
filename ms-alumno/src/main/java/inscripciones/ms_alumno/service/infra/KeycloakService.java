package inscripciones.ms_alumno.service.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    @Autowired
    private KeycloakClient keycloakClient;

    /**
     * Registra un alumno en Keycloak y le asigna el rol correspondiente.
     * @return El ID de Keycloak (UUID) generado.
     */
    public String registrarAlumnoEnKeycloak(String username, String email, String password, String firstName, String lastName) {
        // 1. Obtener Token de administrador para poder operar
        String token = keycloakClient.obtenerToken();

        // 2. Configurar la contraseña
        Map<String, Object> credencial = new HashMap<>();
        credencial.put("type", "password");
        credencial.put("value", password);
        credencial.put("temporary", false);

        List<Map<String, Object>> credenciales = new ArrayList<>();
        credenciales.add(credencial);

        // 3. Construir el cuerpo del usuario para la API de Keycloak
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("username", username);
        usuario.put("email", email);
        usuario.put("emailVerified", true);
        usuario.put("enabled", true);
        usuario.put("firstName", firstName);
        usuario.put("lastName", lastName != null ? lastName : "");
        usuario.put("credentials", credenciales);
        usuario.put("totp", false);
        usuario.put("disableableCredentialTypes", new ArrayList<>());
        usuario.put("requiredActions", new ArrayList<>());

        // 4. Llamar al cliente para crear el usuario y obtener su ID
        String userId = keycloakClient.crearUsuarioEnKeycloak(token, usuario);

        // 5. Asignar el rol "alumno" (El que creaste manualmente)
        asignarRolAlumno(token, userId);

        return userId;
    }

    private void asignarRolAlumno(String token, String userId) {
        // Cambiamos "cliente" por "alumno" para tu caso de universidad
        keycloakClient.asignarRolAUsuario(token, userId, "alumno");
    }

    public void eliminarUsuarioDeKeycloak(String keycloakUserId) {
        String token = keycloakClient.obtenerToken();
        keycloakClient.eliminarUsuarioDeKeycloak(token, keycloakUserId);
    }
}
