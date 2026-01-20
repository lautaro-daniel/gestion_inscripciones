package inscripciones.ms_materias.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MateriaResponseDTO {

    Integer id;
    String nombre;
    String departamento;
    Integer cargaHoraria;
}
