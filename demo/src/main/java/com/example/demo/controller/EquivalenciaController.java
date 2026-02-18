package com.example.demo.controller;

import com.example.demo.model.EquivalenciaCalificacion;
import com.example.demo.service.EquivalenciaCalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/equivalencias")
public class EquivalenciaController {

    @Autowired
    private EquivalenciaCalificacionService equivalenciaService;

    @PostMapping("/legislaciones")
    public ResponseEntity<EquivalenciaCalificacion> crearLegislacion(@RequestBody EquivalenciaCalificacion legislacion) {
        EquivalenciaCalificacion creada = equivalenciaService.crearLegislacion(legislacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/legislaciones/{id}")
    public ResponseEntity<EquivalenciaCalificacion> actualizarLegislacion(@PathVariable String id,
                                                                           @RequestBody EquivalenciaCalificacion legislacion) {
        EquivalenciaCalificacion actualizada = equivalenciaService.actualizarLegislacion(id, legislacion);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/legislaciones/{id}")
    public ResponseEntity<String> eliminarLegislacion(@PathVariable String id) {
        equivalenciaService.eliminarLegislacion(id);
        return ResponseEntity.ok("Legislacion eliminada.");
    }
}
