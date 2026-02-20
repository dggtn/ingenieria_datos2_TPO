package com.example.demo.controller;
import com.example.demo.model.Estudiante;
import com.example.demo.model.ReporteAcademico;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
    public List<ReporteAcademico> getDetalleCompleto(@PathVariable("id") String id){

        return estudianteNeo4jRepository.obtenerDetalleAcademicoPorInstitucion(id);

    }

    @GetMapping("/opciones")
    public List<Map<String, Object>> listarOpcionesEstudiantes() {
        List<Map<String, Object>> salida = new ArrayList<>();
        List<Estudiante> estudiantes = estudianteNeo4jRepository.findAll();
        estudiantes.sort(Comparator.comparing(Estudiante::getNombre, Comparator.nullsLast(String::compareToIgnoreCase)));
        for (Estudiante estudiante : estudiantes) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("id", estudiante.getId());
            fila.put("nombre", estudiante.getNombre());
            salida.add(fila);
        }
        return salida;
    }

    @GetMapping("/{id}/instituciones")
    public List<Map<String, Object>> listarInstitucionesPorEstudiante(@PathVariable("id") String id) {
        return estudianteNeo4jRepository.listarInstitucionesPorEstudiante(id);
    }
}
