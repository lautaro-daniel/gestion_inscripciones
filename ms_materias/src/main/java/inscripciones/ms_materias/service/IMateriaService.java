package inscripciones.ms_materias.service;

import inscripciones.ms_materias.dto.MateriaRequestDTO;
import inscripciones.ms_materias.dto.MateriaResponseDTO;
import inscripciones.ms_materias.model.Materia;

import java.util.List;

public interface IMateriaService {

    MateriaResponseDTO crearMateria(MateriaRequestDTO dto);

    Materia darBajaMateria(Integer id);

    List<MateriaResponseDTO> traerMaterias(Boolean status);

}
