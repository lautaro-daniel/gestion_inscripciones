package inscripciones.ms_alumno.service.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class KeycloakClient {

    private final RestClient restClient;

    @Value("${keycloak.server-url}") // Ejemplo: http://localhost:9000
    private String keycloakServerUrl;

    @Value("${keycloak.realm}") // Ejemplo: universidad-realm
    private String keycloakRealm;

    @Value("${keycloak.admin-username}") // admin
    private String adminUsername;

    @Value("${keycloak.admin-password}") // admin
    private String adminPassword;

    public KeycloakClient() {
        this.restClient = RestClient.create();
    }

    public String obtenerToken() {
        // El token administrativo siempre se pide al realm 'master' para el admin
        String url = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";

        String body = "client_id=admin-cli&username=" + adminUsername +
                "&password=" + adminPassword + "&grant_type=password";

        try {
            Map response = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            return (String) response.get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo token de Keycloak: " + e.getMessage());
        }
    }

    public String crearUsuarioEnKeycloak(String token, Map<String, Object> usuario) {
        String url = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users";

        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(usuario)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        String location = response.getHeaders().getFirst("Location");
                        if (location != null) {
                            return location.substring(location.lastIndexOf('/') + 1);
                        }
                    }
                    throw new RuntimeException("Error al crear usuario. Status: " + response.getStatusCode());
                });
    }

    public void asignarRolAUsuario(String token, String userId, String roleName) {
        // 1. Obtener el ID del rol buscando por nombre
        String urlGetRole = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/roles/" + roleName;
        Map role = restClient.get()
                .uri(urlGetRole)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Map.class);

        // 2. Mapear el rol al usuario
        String urlAssign = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId + "/role-mappings/realm";
        restClient.post()
                .uri(urlAssign)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(List.of(role)) // Keycloak espera una lista de roles
                .retrieve()
                .toBodilessEntity();
    }

    public void eliminarUsuarioDeKeycloak(String token, String userId) {
        String url = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users/" + userId;
        restClient.delete()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}
