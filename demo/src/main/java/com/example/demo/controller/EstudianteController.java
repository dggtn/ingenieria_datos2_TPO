package com.example.demo.controller;
import com.example.demo.model.Estudiante;
import com.example.demo.model.ReporteAcademico;
import com.example.demo.model.RequestModificarEstudiante;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;
    @Autowired
    private EstudianteService estudianteService;


    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(
            @RequestBody RequestRegistrarEstudiante requestRegistrarEstudiante) {

        try {
            Estudiante nuevo = estudianteService.crearEstudiante(
                    requestRegistrarEstudiante);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody RequestRegistrarEstudiante requestRegistrarEstudiante) {
        try {
            Estudiante nuevo = estudianteService.crearEstudiante(requestRegistrarEstudiante);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable("id") String id) {
        Estudiante estudiante = estudianteService.obtenerPorId(id);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudiante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> modificar(
            @PathVariable("id") String id,
            @RequestBody RequestModificarEstudiante requestModificarEstudiante) {
        Estudiante estudiante = estudianteService.modificarEstudiante(id, requestModificarEstudiante);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudiante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") String id) {
        boolean eliminado = estudianteService.eliminarEstudiante(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/detalle-completo")
    public List<ReporteAcademico> getDetalleCompleto(@PathVariable("id") String id){

        return estudianteNeo4jRepository.obtenerDetalleAcademicoPorInstitucion(id);

    }
}
