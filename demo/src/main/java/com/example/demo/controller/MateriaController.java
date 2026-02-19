package com.example.demo.controller;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestAsignarMateriaEstudiante;
import com.example.demo.model.RequestModificarMateria;
import com.example.demo.model.RequestRegistrarMateria;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import com.example.demo.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    @Autowired
    private MateriaNeo4jRepository materiaRepository;
    @Autowired
    private MateriaService materiaService;

    @PostMapping("/registrar")
    public ResponseEntity<Materia> registrar(@RequestBody
                                                  RequestRegistrarMateria requestRegistrarMateria) {

        Materia nueva = materiaService.registrarMateria(
                requestRegistrarMateria);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PostMapping("/estudiantes/{idEstudiante}")
    public ResponseEntity<Materia> crearYAsignarAEstudiante(
            @PathVariable("idEstudiante") String idEstudiante,
            @RequestBody RequestAsignarMateriaEstudiante request) {
        Materia materia = materiaService.crearMateriaYAsignarAEstudiante(idEstudiante, request);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(materia);
    }

    @PutMapping("/{idMateria}")
    public ResponseEntity<Materia> modificarMateria(
            @PathVariable("idMateria") String idMateria,
            @RequestBody RequestModificarMateria request) {
        Materia materia = materiaService.modificarMateria(idMateria, request);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materia);
    }

    @DeleteMapping("/estudiantes/{idEstudiante}/{idMateria}")
    public ResponseEntity<Void> eliminarMateriaDeEstudiante(
            @PathVariable("idEstudiante") String idEstudiante,
            @PathVariable("idMateria") String idMateria) {
        boolean eliminado = materiaService.eliminarMateriaDeEstudiante(idEstudiante, idMateria);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
