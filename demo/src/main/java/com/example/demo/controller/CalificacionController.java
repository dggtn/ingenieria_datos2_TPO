package com.example.demo.controller;

import com.example.demo.model.Calificacion;
import com.example.demo.model.Estudiante;
import com.example.demo.model.RequestRegistrarCalificacion;
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
    private CalificacionService calificacionService;

    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(@RequestBody
                                                RequestRegistrarCalificacion requestRegistrarCalificacion) {

        Estudiante actualizado = calificacionService.registrarCalificacionOriginal(
                requestRegistrarCalificacion);

        return ResponseEntity.status(HttpStatus.CREATED).body(actualizado);
    }

    @PutMapping
    public ResponseEntity<Estudiante> actualizar(@RequestBody RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Estudiante actualizado = calificacionService.actualizarCalificacion(requestRegistrarCalificacion);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping
    public ResponseEntity<Estudiante> eliminar(@RequestParam("estudiante") String estudianteId,
                                               @RequestParam("institucion") String institucionId,
                                               @RequestParam("materia") String materiaId) {
        Estudiante actualizado = calificacionService.eliminarCalificacion(estudianteId, institucionId, materiaId);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/simular-conversion")
    public ResponseEntity<Map<String, Object>> verConversion(
            RequestRegistrarCalificacion requestRegistrarCalificacion) {

        String resultado = calificacionService.calcularConversionSudafrica(requestRegistrarCalificacion);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("pais_origen", requestRegistrarCalificacion.getPaisOrigen());
        respuesta.put("equivalencia_sudafrica", resultado);

        return ResponseEntity.ok(respuesta);
    }
}
