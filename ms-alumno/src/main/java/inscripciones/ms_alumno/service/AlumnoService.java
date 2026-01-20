package inscripciones.ms_alumno.service;

import inscripciones.ms_alumno.dto.NotaDTO;
import inscripciones.ms_alumno.dto.NotaRequestDTO;
import inscripciones.ms_alumno.dto.RegistroAlumnoDTO;
import inscripciones.ms_alumno.ex.AlumnoNotFoundException;
import inscripciones.ms_alumno.model.Alumno;
import inscripciones.ms_alumno.model.Nota;
import inscripciones.ms_alumno.repository.IAlumnoRepo;
import inscripciones.ms_alumno.repository.INotaRepo;
import inscripciones.ms_alumno.service.infra.KeycloakService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlumnoService implements IAlumnoService{

    IAlumnoRepo iAlumnoRepo;
    INotaRepo iNotaRepo;
    KeycloakService keycloakService;

    @Override
    @Transactional
    public Alumno registrar(RegistroAlumnoDTO dto) {
        // Hacer el registro en keycloak y obtener el id externo
        String keycloakId = keycloakService.registrarAlumnoEnKeycloak(
                dto.getEmail(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getNombre(),
                dto.getApellido()
        );

        Alumno alumno = Alumno.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .legajo(dto.getLegajo())
                .idKeycloak(keycloakId).build();

        return iAlumnoRepo.save(alumno);
    }

    @Override
    public List<Alumno> listarTodos() {
        return iAlumnoRepo.findAll();
    }

    @Override
    public Alumno buscarPorId(Integer id) {
        return iAlumnoRepo.findById(id)
                .orElseThrow(() -> new AlumnoNotFoundException("No se encontró un Alumno con ID" + id));
    }

    @Override
    @Transactional
    public NotaDTO actualizarNota(Integer notaId, Double nuevoValor) {
        // 1. Buscar la nota existente o lanzar error si no existe
        Nota nota = iNotaRepo.findById(notaId)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró la nota con ID: " + notaId));

        // 2. Actualizar el valor (Hibernate detectará el cambio al terminar el método)
        nota.setValor(nuevoValor);

        // 3. Guardar los cambios
        Nota notaActualizada = iNotaRepo.save(nota);
        
        NotaDTO response =  NotaDTO.builder()
                .id(notaActualizada.getId())
                .valor(notaActualizada.getValor())
                .materia(notaActualizada.getMateria())
                .build();

        if (notaActualizada.getAlumno() != null){
            response.setNombreAlumno(notaActualizada.getAlumno().getNombre());
            response.setLegajoAlumno(notaActualizada.getAlumno().getLegajo());
        }else {
            response.setNombreAlumno("Sin Asignar");
            response.setLegajoAlumno("N/A");
        }
        return response;
    }

    @Override
    @Transactional
    public NotaDTO crearNota(NotaRequestDTO dto) {
        // 1. Buscar al alumno por ID
        Alumno alumno = iAlumnoRepo.findById(dto.getAlumnoId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + dto.getAlumnoId()));

        // 2. Crear y guardar la entidad Nota
        Nota nuevaNota = Nota.builder()
                .valor(dto.getValor())
                .materia(dto.getMateria())
                .alumno(alumno) // Vínculo esencial
                .build();

        Nota notaGuardada = iNotaRepo.save(nuevaNota);

        // 3. Convertir a DTO para la respuesta
        return NotaDTO.builder()
                .id(notaGuardada.getId())
                .valor(notaGuardada.getValor())
                .materia(notaGuardada.getMateria())
                .nombreAlumno(alumno.getNombre())
                .legajoAlumno(alumno.getLegajo())
                .build();
    }

    @Override
    public List<Nota> verNotas() {
        return iNotaRepo.findAll();
    }
}
