package com.example.demo.controller;
import com.example.demo.model.Estudiante;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteMONGORepository estudianteMONGORepository;
    @Autowired
    private CalificacionMONGORepository calificacionMONGORepository;
    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;
    @Autowired
    private EstudianteService estudianteService;

    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(
            @RequestBody RequestRegistrarEstudiante requestRegistrarEstudiante) {

        Estudiante nuevo = estudianteService.registrarEstudiante(
                requestRegistrarEstudiante);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PostMapping("/Historial")
    public ResponseEntity<String> guardarHistorial(
            @RequestBody String estudianteId,
             @RequestBody String institucionId){

        estudianteService.asociarInstitucionPrevia(estudianteId,institucionId);

        return ResponseEntity.ok("Vínculo histórico creado en Neo4j entre el estudiante y la institución.");
    }

    @GetMapping("/historial/{id}")
    public ResponseEntity<List<Map<String, Object>>> verHistorial(@PathVariable String id) {
        List<Map<String, Object>> historial = estudianteService.consultarHistorial(id);
        return ResponseEntity.ok(historial);
    }


}
