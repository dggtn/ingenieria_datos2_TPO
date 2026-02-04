package com.example.demo.neo4j.servicios;

import com.example.demo.neo4j.entidad.Estudiante;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstudianteServicio implements IEstudianteServicio {

    @Override
    public ResponseEntity<Estudiante> getEstudiantes() {
        return null;
    }

    @Override
    public Optional<Estudiante> getEstudianteById(Long estudianteId) {
        return Optional.empty();
    }

    @Override
    public Estudiante crearEstudiante(Object o) {
        return null;
    }
}