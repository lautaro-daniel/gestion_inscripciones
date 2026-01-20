package inscripciones.ms_materias.controller;

import inscripciones.ms_materias.dto.MateriaRequestDTO;
import inscripciones.ms_materias.dto.MateriaResponseDTO;
import inscripciones.ms_materias.model.Materia;
import inscripciones.ms_materias.service.IMateriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final IMateriaService service;

    public MateriaController(IMateriaService service) {
        this.service = service;
    }

    @GetMapping("/ver")
    public ResponseEntity<List<MateriaResponseDTO>> verMaterias(
            @RequestParam(defaultValue = "true") Boolean status
    ){
        List<MateriaResponseDTO> lista = service.traerMaterias(status);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/registrar")
    public ResponseEntity<MateriaResponseDTO> registrarMateria(@RequestBody MateriaRequestDTO dto){
        return ResponseEntity.ok(service.crearMateria(dto));
    }

    @DeleteMapping("/registrarBaja/{id}")
    public ResponseEntity<Materia> registrarBajaMateria(@PathVariable Integer id){
        service.darBajaMateria(id);
        return ResponseEntity.noContent().build();
    }
}
