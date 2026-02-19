package com.example.demo.controller;

import com.example.demo.model.Institucion;
import com.example.demo.model.RequestCurso;
import com.example.demo.model.RequestModificarCurso;
import com.example.demo.model.RequestModificarInstitucion;
import com.example.demo.model.RequestRegistrarInstitucion;
import com.example.demo.service.InstitucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/instituciones")
public class InstitucionController {

    @Autowired
    private InstitucionService institucionService;

    @PostMapping("/registrar")
    public ResponseEntity<Institucion> registrar(
            @RequestBody RequestRegistrarInstitucion requestRegistrarInstitucion) {

        Institucion nueva = institucionService.crearInstitucion(
                requestRegistrarInstitucion);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PostMapping
    public ResponseEntity<Institucion> crear(@RequestBody RequestRegistrarInstitucion requestRegistrarInstitucion) {
        Institucion nueva = institucionService.crearInstitucion(requestRegistrarInstitucion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institucion> obtenerPorId(@PathVariable("id") String id) {
        Institucion institucion = institucionService.obtenerPorId(id);
        if (institucion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(institucion);
    }

    @GetMapping("/{id}/curriculum")
    public ResponseEntity<java.util.List<Institucion.Curso>> obtenerCurriculum(@PathVariable("id") String id) {
        java.util.List<Institucion.Curso> curriculum = institucionService.obtenerCurriculum(id);
        if (curriculum == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(curriculum);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Institucion> modificar(
            @PathVariable("id") String id,
            @RequestBody RequestModificarInstitucion requestModificarInstitucion) {
        Institucion institucion = institucionService.modificarInstitucion(id, requestModificarInstitucion);
        if (institucion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(institucion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") String id) {
        boolean eliminado = institucionService.eliminarInstitucion(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idInstitucion}/cursos")
    public ResponseEntity<Institucion> crearCursoYAsignar(
            @PathVariable("idInstitucion") String idInstitucion,
            @RequestBody RequestCurso requestCurso) {
        Institucion institucion = institucionService.crearCursoYAsignarAInstitucion(idInstitucion, requestCurso);
        if (institucion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(institucion);
    }

    @PutMapping("/{idInstitucion}/cursos/{idCurso}")
    public ResponseEntity<Institucion> modificarCurso(
            @PathVariable("idInstitucion") String idInstitucion,
            @PathVariable("idCurso") String idCurso,
            @RequestBody RequestModificarCurso requestModificarCurso) {
        Institucion institucion = institucionService.modificarCurso(idInstitucion, idCurso, requestModificarCurso);
        if (institucion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(institucion);
    }

    @DeleteMapping("/{idInstitucion}/cursos/{idCurso}")
    public ResponseEntity<Void> eliminarCurso(
            @PathVariable("idInstitucion") String idInstitucion,
            @PathVariable("idCurso") String idCurso) {
        boolean eliminado = institucionService.eliminarCursoDeInstitucion(idInstitucion, idCurso);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}


