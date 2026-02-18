package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @PostMapping("/registrar")
    public ResponseEntity<Estudiante> registrar(@RequestBody Estudiante estudiante) {
        Estudiante nuevo = estudianteService.registrarEstudiante(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PostMapping("/registrar-multiple")
    public ResponseEntity<List<Estudiante>> registrarMultiple(@RequestBody List<Estudiante> estudiantes) {
        List<Estudiante> nuevos = estudianteService.registrarEstudiantes(estudiantes);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        Estudiante actualizado = estudianteService.actualizarEstudiante(id, estudiante);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable String id) {
        estudianteService.eliminarEstudiante(id);
        return ResponseEntity.ok("Estudiante eliminado.");
    }

    @DeleteMapping("/eliminar-multiple")
    public ResponseEntity<String> eliminarMultiple(@RequestBody List<String> ids) {
        estudianteService.eliminarEstudiantes(ids);
        return ResponseEntity.ok("Estudiantes eliminados.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @GetMapping("/{id}/reporte-trayectoria")
    public ResponseEntity<Map<String, Object>> obtenerReporteTrayectoria(@PathVariable String id) {
        return ResponseEntity.ok(estudianteService.obtenerReporteTrayectoriaOrdenada(id));
    }

    @GetMapping("/{id}/promedios-sudafrica")
    public ResponseEntity<Map<String, Object>> obtenerPromediosSudafrica(@PathVariable String id) {
        return ResponseEntity.ok(estudianteService.obtenerPromediosSudafricaPorNivel(id));
    }

}

