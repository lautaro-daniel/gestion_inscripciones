package inscripciones.ms_alumno.service;

import inscripciones.ms_alumno.dto.NotaDTO;
import inscripciones.ms_alumno.dto.NotaRequestDTO;
import inscripciones.ms_alumno.dto.RegistroAlumnoDTO;
import inscripciones.ms_alumno.model.Alumno;
import inscripciones.ms_alumno.model.Nota;

import java.util.List;

public interface IAlumnoService {

    Alumno registrar(RegistroAlumnoDTO registroDTO);
    List<Alumno> listarTodos();
    Alumno buscarPorId(Integer id);

    NotaDTO actualizarNota(Integer id, Double nuevoValor);

    NotaDTO crearNota(NotaRequestDTO dto);

    List<Nota> verNotas();

}
