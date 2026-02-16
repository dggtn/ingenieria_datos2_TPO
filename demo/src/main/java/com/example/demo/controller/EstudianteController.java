package com.example.demo.controller;
import com.example.demo.model.Estudiante;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;
    @Autowired
    private CalificacionMONGORepository calificacionMONGORepository;
    @Autowired
    private EstudianteService estudianteService;


    @Autowired
    private EstudianteNeo4jRepository estudianteMongoRepo;

    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(
            @RequestBody RequestRegistrarEstudiante requestRegistrarEstudiante) {

        Estudiante nuevo = estudianteService.registrarEstudiante(
                requestRegistrarEstudiante);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

//    @PostMapping("/sincronizar-historial/{id}")
//    public ResponseEntity<String> guardarHistorial(@PathVariable String id) {
//        UUID uuid = UUID.fromString(id);
//        return estudianteMongoRepo.findById(String.valueOf(uuid))
//                .map(estudiante -> {
//                    historialService.sincronizarHistorialAMongo(estudiante);
//                    return ResponseEntity.ok("Sincronización de historial a Neo4j exitosa.");
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }

//    @GetMapping("/historial/{id}")
//    public ResponseEntity<List<Map<String, Object>>> verHistorial(@PathVariable String id) {
//        List<Map<String, Object>> historial = historialService.getEstudianteNeo4jRepo()
//                .obtenerHistorialAcademico(UUID.fromString(id));
//        return ResponseEntity.ok(historial);
//    }


}
