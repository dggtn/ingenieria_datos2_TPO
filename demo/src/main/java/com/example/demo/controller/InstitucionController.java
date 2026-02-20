package com.example.demo.controller;

import com.example.demo.model.Institucion;
import com.example.demo.model.InstitucionOpcion;
import com.example.demo.model.RequestRegistrarInstitucion;
import com.example.demo.service.InstitucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/instituciones")
public class InstitucionController {

    @Autowired
    private InstitucionService institucionService;

    @PostMapping("/registrar")
    public ResponseEntity<Institucion> registrar(
            @RequestBody RequestRegistrarInstitucion requestRegistrarInstitucion) {

        Institucion nueva = institucionService.registrarInstitucion(
                requestRegistrarInstitucion);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institucion> obtenerPorId(@PathVariable("id") String id) {
        return institucionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/opciones")
    public List<InstitucionOpcion> listarOpcionesInstituciones() {
        return institucionService.listarOpcionesInstituciones();
    }
}


