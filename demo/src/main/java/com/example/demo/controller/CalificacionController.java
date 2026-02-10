package com.example.demo.controller;

import com.example.demo.model.Calificacion;
import com.example.demo.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/calificaciones")
public class CalificacionController {
    @Autowired
    private CalificacionService service;

    @PostMapping("/registrar")
    public Calificacion registrarCalificacion(@RequestParam String nota,@RequestParam String tipo) {
        return service.regstrarCalificacion(nota,tipo);
    }

    public Calificacion convertirCalificacion() {
        return null;
    }
}

