package com.example.demo.controller;


import com.example.demo.model.Estudiante;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.neo4j.InstitucionRepository;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteMONGORepository estudianteRepository;
    @Autowired
    private CalificacionMONGORepository calificacionRepository;
    @Autowired
    private InstitucionRepository institucionRepository;
    @Autowired
    private EstudianteService estudianteService;

    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(
            @RequestParam String institucionId,
            @RequestBody(required = false) Map<String, Object> metadatos ){

        Estudiante nuevo = estudianteService.registrarEstudiante(
                institucionId, metadatos);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}
