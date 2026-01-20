package inscripciones.ms_alumno.controller;

import inscripciones.ms_alumno.dto.ModificarNotaDTO;
import inscripciones.ms_alumno.dto.NotaDTO;
import inscripciones.ms_alumno.dto.NotaRequestDTO;
import inscripciones.ms_alumno.dto.RegistroAlumnoDTO;
import inscripciones.ms_alumno.model.Alumno;
import inscripciones.ms_alumno.model.Nota;
import inscripciones.ms_alumno.service.IAlumnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/alumnos")
@RestController
public class AlumnoController {

    private final IAlumnoService service;

    public AlumnoController(IAlumnoService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Alumno> registrar(@RequestBody RegistroAlumnoDTO dto) {
        return new ResponseEntity<>(service.registrar(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Alumno>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }


    // CREAR NOTA (Solo Profesor)
    @PostMapping("/notas")
    public ResponseEntity<NotaDTO> crearNota(@RequestBody NotaRequestDTO dto) {
        return new ResponseEntity<>(service.crearNota(dto), HttpStatus.CREATED);
    }

    // MODIFICAR NOTA (Solo Profesor)
    @PutMapping("/notas/{notaId}")
    public ResponseEntity<NotaDTO> modificarNota(
            @PathVariable Integer notaId,
            @RequestBody ModificarNotaDTO dto) {

        return ResponseEntity.ok(service.actualizarNota(notaId, dto.getNuevoValor()));
    }

    @GetMapping("/notas/ver")
    public ResponseEntity<List<Nota>> verNotas(){
        return ResponseEntity.ok(service.verNotas());
    }

    @GetMapping("/traer/{id}")
    public ResponseEntity<Alumno> traerPorId(@PathVariable Integer id){
        Alumno alumno = service.buscarPorId(id);
        return ResponseEntity.ok(alumno);
    }

}
