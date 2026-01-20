package inscripciones.ms_alumno.dto;

import lombok.Data;

@Data
public class NotaRequestDTO {
    private Double valor;
    private String materia;
    private Integer alumnoId; // Clave para vincular al alumno en la DB
}
