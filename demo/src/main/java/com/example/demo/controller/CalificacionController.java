package com.example.demo.controller;

import com.example.demo.model.Calificacion;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
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
    private EstudianteMONGORepository estudianteRepository;
    @Autowired
    private CalificacionMONGORepository calificacionRepository;
    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/registrar")
    public ResponseEntity<Calificacion> registrar(
            @RequestParam String estudianteId,
            @RequestParam String materiaId,
            @RequestParam String pais,
            @RequestParam String notaBase,
            @RequestBody(required = false) Map<String, Object> metadatos) {

        if (metadatos == null) {
            metadatos = new HashMap<>();
        }

        String notaSudafrica = calificacionService.calcularConversionSudafrica(notaBase, pais, metadatos);

        metadatos.put("equivalencia_sudafrica", notaSudafrica);

        Calificacion nueva = calificacionService.registrarCalificacionOriginal(
                estudianteId, materiaId, pais, notaBase, metadatos);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/simular-conversion")
    public ResponseEntity<Map<String, String>> verConversion(
            @RequestParam String nota,
            @RequestParam String pais) {

        String resultado = calificacionService.calcularConversionSudafrica(nota, pais, null);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("pais_origen", pais);
        respuesta.put("nota_original", nota);
        respuesta.put("equivalencia_sudafrica", resultado);

        return ResponseEntity.ok(respuesta);
    }


}

