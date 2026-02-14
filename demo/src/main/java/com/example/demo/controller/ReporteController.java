package com.example.demo.controller;
import com.example.demo.model.ReportePromedio;
import com.example.demo.service.ReporteServcio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteServcio reporteServicio;

    @GetMapping
    public ResponseEntity<ReportePromedio> mostrar(
            @RequestParam String pais) {

        ReportePromedio nuevo = reporteServicio.generarReporteOficial(pais, new Date());

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}
