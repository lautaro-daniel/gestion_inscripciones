package inscripciones.ms_alumno.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Registro abierto
                        .requestMatchers("/api/alumnos/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/alumnos/notas/**").hasRole("profesor")
                        .requestMatchers(HttpMethod.GET, "/api/alumnos/{id}").hasAnyRole("alumno", "profesor")

                        // 2. Modificar notas: SOLO PROFESORES
                        // Usamos el método HTTP PUT o PATCH para representar "modificación"
                        .requestMatchers(HttpMethod.PUT, "/api/alumnos/notas/**").hasRole("profesor")
                        .requestMatchers(HttpMethod.PATCH, "/api/alumnos/notas/**").hasRole("profesor")

                        // 3. Ver notas: Alumnos y Profesores
                        .requestMatchers(HttpMethod.GET, "/api/alumnos/notas/**").hasAnyRole("alumno", "profesor")
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // 1. Cargamos las autoridades por defecto (Scopes)
            var defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = defaultAuthoritiesConverter.convert(jwt);

            // 2. Extraemos los roles de Keycloak (ubicados en realm_access.roles)
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
                authorities.addAll(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
                        .collect(Collectors.toList()));
            }

            return authorities;
        });

        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Usamos 127.0.0.1 que es más estable en Windows para evitar demoras de DNS
        String issuerUri = "http://localhost:9000/realms/inscripciones";

        // Configuramos un cliente HTTP con tiempos de espera extendidos
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000); // 10 segundos
        requestFactory.setReadTimeout(10000);    // 10 segundos
        restTemplate.setRequestFactory(requestFactory);

        // Construimos el decoder usando nuestro cliente personalizado
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri)
                .restOperations(restTemplate)
                .build();

        return jwtDecoder;
    }
}
