package com.example.demo.controllers;


import com.example.demo.cassandra.entidad.Estudiante;
import com.example.demo.cassandra.servicios.IEstudianteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("estudiantes")
public class EstudianteController {

        @Autowired
        private IEstudianteServicio estudianteServicio;

        @GetMapping
        public ResponseEntity<Estudiante> getEstudiante(){
                return estudianteServicio.getEstudiantes();
        }


        @GetMapping("/{estudianteId}/trayectoria")
        public ResponseEntity<Estudiante> getEstudianteById(@PathVariable Long estudianteId) {
            Optional<Estudiante> result = estudianteServicio.getEstudianteById(estudianteId);
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());

        }


    }




