package com.example.demo.controllers;

import com.example.demo.cassandra.entidad.Calificacion;

import com.example.demo.cassandra.dto.CalificacionDTO;

import com.example.demo.cassandra.servicios.ICalificacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

public class CalificacionController {

    @Autowired
    private ICalificacionServicio calificacionServicio;
    @PostMapping
    public ResponseEntity<Object> registrarCalificacion(@RequestBody CalificacionDTO calificacionRequest){
        Calificacion result = calificacionServicio.crearCalificacion(calificacionRequest.registrarCalificacion());
        return ResponseEntity.created(URI.create("/{id}/calificacion" + result.getId())).body(result);
    }

}
