package inscripciones.ms_materias.ex;

import inscripciones.ms_materias.dto.MateriaResponseDTO;

public class MateriaNotFoundException extends RuntimeException{

    public MateriaNotFoundException(String message){

        super(message);
    }
}
