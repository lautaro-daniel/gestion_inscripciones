package inscripciones.ms_alumno.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {
    private Integer id;
    private Double valor;
    private String materia;
    private String nombreAlumno;
    private String legajoAlumno;
}