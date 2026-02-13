package com.example.demo.controller;

import com.example.demo.model.Institucion;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.neo4j.InstitucionRepository;
import com.example.demo.service.InstitucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/instituciones")
public class InstitucionController {
    @Autowired
    private EstudianteMONGORepository estudianteRepository; // Neo4j
    @Autowired
    private CalificacionMONGORepository calificacionRepository; // MongoDB
    @Autowired
    private InstitucionRepository institucionRepository; // Neo4j
    @Autowired
    private InstitucionService institucionService;

    @PostMapping("/registrar")
    public ResponseEntity<Institucion> registrar(
            @RequestParam String institucionId,
            @RequestBody(required = false) Map<String, Object> metadatos ){

        Institucion nuevo = institucionService.registrarEstudiante(
                institucionId, metadatos);


        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

}
