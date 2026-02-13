package com.example.demo.service;

import com.example.demo.model.Calificacion;
import com.example.demo.model.Estudiante;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteMONGORepository estudianteRepository;
    public Estudiante registrarEstudiante(String institucionId, Map<String, Object> metadatos) {
        Estudiante e = new Estudiante();
        e.setInstitucionActual(institucionId);
        return estudianteRepository.save(e);
    }
}
