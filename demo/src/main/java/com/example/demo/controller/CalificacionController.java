package com.example.demo.controller;

import com.example.demo.model.Calificacion;
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

    // Registra una calificacion y actualiza los datos asociados en las bases.
    @PostMapping("/registrar")
    public ResponseEntity<Calificacion> registrar(@RequestBody RequestRegistrarCalificacion requestRegistrarCalificacion) {

        Calificacion nueva = calificacionService.registrarCalificacionOriginal(requestRegistrarCalificacion);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // Modifica una calificacion existente por ID.
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> modificar(@PathVariable("id") String id,
                                                  @RequestBody RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion actualizada = calificacionService.modificarCalificacion(id, requestRegistrarCalificacion);
        return ResponseEntity.ok(actualizada);
    }

    // Simula la conversion de una nota a escala Sudafrica sin persistir cambios.
    @PostMapping("/simular-conversion")
    public ResponseEntity<Map<String, Object>> verConversion(@RequestBody RequestRegistrarCalificacion requestRegistrarCalificacion) {

        Double resultado = calificacionService.calcularConversionSudafrica(requestRegistrarCalificacion);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("pais_origen", requestRegistrarCalificacion.getPaisOrigen());
        respuesta.put("equivalencia_sudafrica", resultado);

        return ResponseEntity.ok(respuesta);
    }
}
