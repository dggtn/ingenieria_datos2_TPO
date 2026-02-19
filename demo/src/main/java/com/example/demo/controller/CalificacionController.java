package com.example.demo.controller;

import com.example.demo.model.Calificacion;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestCalificacionFinal;
import com.example.demo.model.RequestCalificacionParcial;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {
    @Autowired
    private EstudianteNeo4jRepository estudianteRepository;
    @Autowired
    private CalificacionMONGORepository calificacionRepository;
    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/registrar")
    public ResponseEntity<Calificacion> registrar(@RequestBody
                                                  RequestRegistrarCalificacion requestRegistrarCalificacion) {

        Calificacion nueva = calificacionService.registrarCalificacionOriginal(
                requestRegistrarCalificacion);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/simular-conversion")
    public ResponseEntity<Map<String, Object>> verConversion(
            RequestRegistrarCalificacion requestRegistrarCalificacion) {

        Double resultado = calificacionService.calcularConversionSudafrica(requestRegistrarCalificacion);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("pais_origen", requestRegistrarCalificacion.getPaisOrigen());
        respuesta.put("equivalencia_sudafrica", resultado);

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/estudiantes/{idEstudiante}/materias/{idMateria}/parciales")
    public ResponseEntity<Materia> crearParcial(
            @PathVariable("idEstudiante") String idEstudiante,
            @PathVariable("idMateria") String idMateria,
            @RequestBody RequestCalificacionParcial request
    ) {
        Materia materia = calificacionService.agregarCalificacionParcial(idEstudiante, idMateria, request);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(materia);
    }

    @PutMapping("/estudiantes/{idEstudiante}/materias/{idMateria}/final")
    public ResponseEntity<Materia> asignarFinal(
            @PathVariable("idEstudiante") String idEstudiante,
            @PathVariable("idMateria") String idMateria,
            @RequestBody RequestCalificacionFinal request
    ) {
        Materia materia = calificacionService.asignarCalificacionFinal(idEstudiante, idMateria, request);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materia);
    }

    @PutMapping("/estudiantes/{idEstudiante}/materias/{idMateria}/parciales/{nombreParcial}")
    public ResponseEntity<Materia> modificarParcial(
            @PathVariable("idEstudiante") String idEstudiante,
            @PathVariable("idMateria") String idMateria,
            @PathVariable("nombreParcial") String nombreParcial,
            @RequestBody RequestCalificacionParcial request
    ) {
        Materia materia = calificacionService.modificarCalificacionParcial(idEstudiante, idMateria, nombreParcial, request);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materia);
    }

    @DeleteMapping("/estudiantes/{idEstudiante}/materias/{idMateria}/parciales/{nombreParcial}")
    public ResponseEntity<Materia> eliminarParcial(
            @PathVariable("idEstudiante") String idEstudiante,
            @PathVariable("idMateria") String idMateria,
            @PathVariable("nombreParcial") String nombreParcial
    ) {
        Materia materia = calificacionService.eliminarCalificacionParcial(idEstudiante, idMateria, nombreParcial);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materia);
    }


}

