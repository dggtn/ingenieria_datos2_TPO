package com.example.demo.controller;


import com.example.demo.repository.mongo.CalificacionRepository;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.neo4j.InstitucionRepository;
import com.example.demo.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteMONGORepository estudianteRepository; // Neo4j
    @Autowired
    private CalificacionRepository calificacionRepository; // MongoDB
    @Autowired
    private InstitucionRepository institucionRepository; // Neo4j
    @Autowired
    private CalificacionService calificacionService;


}
