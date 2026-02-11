package com.example.demo.controller;

import com.example.demo.model.Calificacion;
import com.example.demo.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/registrar")
    public ResponseEntity<Calificacion> registrar(
            @RequestParam String estudianteId,
            @RequestParam String materiaId,
            @RequestParam String pais,
            @RequestParam String notaBase,
            @RequestBody Map<String, Object> metadatos) {

        Calificacion nueva = calificacionService.registrarCalificacionOriginal(
                estudianteId, materiaId, pais, notaBase, metadatos);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
}

