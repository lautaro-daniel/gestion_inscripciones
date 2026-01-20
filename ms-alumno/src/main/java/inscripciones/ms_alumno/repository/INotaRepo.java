package inscripciones.ms_alumno.repository;

import inscripciones.ms_alumno.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotaRepo extends JpaRepository<Nota, Integer> {
}
