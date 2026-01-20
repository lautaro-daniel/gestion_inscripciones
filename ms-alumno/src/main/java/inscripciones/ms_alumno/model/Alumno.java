package inscripciones.ms_alumno.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "ALUMNOS")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "ID_ALUMNO")
    Integer id;
    @Column(name = "NOMBRE")
    String nombre;
    @Column(name = "APELLIDO")
    String apellido;
    @Column(name = "ID_EXTERNO_KEYCLOAK")
    String idKeycloak;
    @Column(name = "LEGAJO", unique = true)
    String legajo;
    @Column(name = "EMAIL")
    String email;

}
