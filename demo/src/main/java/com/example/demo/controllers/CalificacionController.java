package com.example.demo.controllers;

import com.example.demo.cassandra.servicios.ICalificacionServicio;
import com.example.demo.mongo.dto.CalificacionDTO;
import com.example.demo.mongo.entidad.Calificacion;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "Calificaciones", description = "Endpoints para el registro y conversión de notas oficiales")
public class CalificacionController {

    @Autowired
    private ICalificacionServicio calificacionServicio;
    @PostMapping
    public Calificacion registrarCalificacion(@RequestBody CalificacionDTO calificacionRequest){
        Calificacion result = calificacionServicio.registrarCalificacion();
        return result;
    }

}
