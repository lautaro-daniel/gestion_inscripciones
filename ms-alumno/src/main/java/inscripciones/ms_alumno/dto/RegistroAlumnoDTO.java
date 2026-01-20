package inscripciones.ms_alumno.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistroAlumnoDTO {

    @Schema(description = "Nombre del alumno", example = "Juan")
    String nombre;
    @Schema(description = "Apellido del alumno", example = "Gomez")
    String apellido;
    @Schema(description = "Email institucional del alumno", example = "juan@facultad.com")
    String email;
    @Schema(description = "Identificacion en la universidad del alumno", example = "409876")
    String legajo;
    @Schema(description = "Password al sistema de la universidad virtual", example = "clave123")
    String password;
}
