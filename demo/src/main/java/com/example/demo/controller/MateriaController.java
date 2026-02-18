package com.example.demo.controller;

import com.example.demo.model.Materia;
import com.example.demo.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    @Autowired
    private MateriaService materiaService;

    @PostMapping("/registrar")
    public ResponseEntity<Materia> registrar(@RequestParam String institucionId, @RequestBody Materia materia) {
        Materia nueva = materiaService.registrarMateria(institucionId, materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PostMapping("/registrar-multiple")
    public ResponseEntity<List<Materia>> registrarMultiple(@RequestParam String institucionId, @RequestBody List<Materia> materias) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.registrarMaterias(institucionId, materias));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> actualizar(@RequestParam String institucionId, @PathVariable String id, @RequestBody Materia materia) {
        return ResponseEntity.ok(materiaService.actualizarMateria(institucionId, id, materia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@RequestParam String institucionId, @PathVariable String id) {
        materiaService.eliminarMateria(institucionId, id);
        return ResponseEntity.ok("Materia eliminada.");
    }

    @DeleteMapping("/eliminar-multiple")
    public ResponseEntity<String> eliminarMultiple(@RequestParam String institucionId, @RequestBody List<String> ids) {
        materiaService.eliminarMaterias(institucionId, ids);
        return ResponseEntity.ok("Materias eliminadas.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> obtenerPorId(@RequestParam String institucionId, @PathVariable String id) {
        return ResponseEntity.ok(materiaService.obtenerPorId(institucionId, id));
    }
}
