package inscripciones.ms_materias.service;

import inscripciones.ms_materias.dto.MateriaRequestDTO;
import inscripciones.ms_materias.dto.MateriaResponseDTO;
import inscripciones.ms_materias.ex.MateriaNotFoundException;
import inscripciones.ms_materias.model.Materia;
import inscripciones.ms_materias.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MateriaService implements IMateriaService {

    private final MateriaRepository repo;

    public MateriaService(MateriaRepository repo) {
        this.repo = repo;
    }
    @Override
    @Transactional
    public MateriaResponseDTO crearMateria(MateriaRequestDTO dto) {

        Materia materia = Materia.builder()
                .nombre(dto.getNombre())
                .departamento(dto.getDepartamento())
                .cargaHoraria(dto.getCargaHoraria())
                .isActive(true)
                .build();

        Materia savedMateria = repo.save(materia);

        return MateriaResponseDTO.builder()
                .id(savedMateria.getId())
                .nombre(savedMateria.getNombre())
                .departamento(savedMateria.getDepartamento())
                .cargaHoraria(savedMateria.getCargaHoraria())
                .build();
    }

    @Override
    @Transactional
    public Materia darBajaMateria(Integer id) {
        Materia materia = repo.findById(id).orElseThrow(() -> new MateriaNotFoundException
                ("Materia con id " + id +" no encontrada."));
        materia.setIsActive(false);

        return materia;
    }

    @Override
    public List<MateriaResponseDTO> traerMaterias(Boolean status) {

        List<Materia> materias = repo.materiasActivas(status);
        return materias.stream()
                .map(m -> MateriaResponseDTO.builder()
                        .id(m.getId())
                        .nombre(m.getNombre())
                        .departamento(m.getDepartamento())
                        .cargaHoraria(m.getCargaHoraria())
                        .build())
                .toList();
    }
}
