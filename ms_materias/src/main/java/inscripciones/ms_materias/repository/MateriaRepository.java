package inscripciones.ms_materias.repository;

import inscripciones.ms_materias.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    @Query("SELECT m FROM Materia m WHERE m.isActive = :active")
    List<Materia> materiasActivas(@Param("active") Boolean active);
}
