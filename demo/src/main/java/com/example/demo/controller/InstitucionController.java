package com.example.demo.controller;

import com.example.demo.model.Institucion;
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
    public ResponseEntity<Institucion> registrar(@RequestBody Institucion institucion) {
        Institucion nueva = institucionService.registrarInstitucion(institucion);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PostMapping("/registrar-multiple")
    public ResponseEntity<List<Institucion>> registrarMultiple(@RequestBody List<Institucion> instituciones) {
        return ResponseEntity.status(HttpStatus.CREATED).body(institucionService.registrarInstituciones(instituciones));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Institucion> actualizar(@PathVariable String id, @RequestBody Institucion institucion) {
        return ResponseEntity.ok(institucionService.actualizarInstitucion(id, institucion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable String id) {
        institucionService.eliminarInstitucion(id);
        return ResponseEntity.ok("Institucion eliminada.");
    }

    @DeleteMapping("/eliminar-multiple")
    public ResponseEntity<String> eliminarMultiple(@RequestBody List<String> ids) {
        institucionService.eliminarInstituciones(ids);
        return ResponseEntity.ok("Instituciones eliminadas.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institucion> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(institucionService.obtenerPorId(id));
    }
}


