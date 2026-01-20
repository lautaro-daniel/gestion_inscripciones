package inscripciones.ms_alumno.repository;

import inscripciones.ms_alumno.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlumnoRepo extends JpaRepository<Alumno, Integer> {
}
