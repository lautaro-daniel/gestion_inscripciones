package inscripciones.ms_materias.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MateriaRequestDTO {

    @Schema(description = "Nombre de la materia", example = "Sintaxis y semántica de los lenguajes")
    String nombre;
    @Schema(description = "Departamento al que pertenece la materia", example = "Departamento de Sistemas")
    String departamento;
    @Schema(description = "Carga horaria por semana de la materia", example = "6")
    Integer cargaHoraria;
}
