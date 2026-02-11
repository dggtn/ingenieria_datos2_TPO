package com.example.demo.controller;

import com.example.demo.model.Calificacion;
import com.example.demo.model.Estudiante;
import com.example.demo.model.dto.TrayectoriaAcademica;
import com.example.demo.repository.mongo.EstudianteRepository;
import com.example.demo.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/{id}/revalidar-nota")
    public Estudiante revalidarNota(
            @PathVariable String id,
            @RequestParam String nota,
            @RequestParam String pais) {

        Estudiante estudiante = estudianteRepository.findById(id);

        Calificacion calificacionProcesada = calificacionService.convertirYGuardarConversion(nota, pais);

        if (!estudiante.getCalificacionIds().contains(calificacionProcesada.getId())) {
            estudiante.getCalificacionIds().add(calificacionProcesada.getId());
        }

        return estudianteRepository.save(estudiante);
    }

}

