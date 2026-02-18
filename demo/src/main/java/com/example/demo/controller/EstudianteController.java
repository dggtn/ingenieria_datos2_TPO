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

import java.util.List;
import java.util.Map;

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



    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(
            @RequestBody RequestRegistrarEstudiante requestRegistrarEstudiante) {

        Estudiante nuevo = estudianteService.registrarEstudiante(
                requestRegistrarEstudiante);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}/detalle-completo")
    public ResponseEntity<?> getDetalleCompleto(@PathVariable String id) {
        try {
            List<Map<String, Object>> detalle = estudianteNeo4jRepository.obtenerDetalleAcademicoPorInstitucion(id);
            if (detalle.isEmpty()) {
                return ResponseEntity.status(404).body("No se encontró actividad académica para el ID: " + id);
            }
            return ResponseEntity.ok(detalle);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en el grafo: " + e.getMessage());
        }
    }

}
