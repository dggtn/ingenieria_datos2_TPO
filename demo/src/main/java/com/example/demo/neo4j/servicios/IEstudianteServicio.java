package com.example.demo.neo4j.servicios;


import com.example.demo.neo4j.entidad.Estudiante;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Optional;
@Service
public interface IEstudianteServicio {

    ResponseEntity<Estudiante> getEstudiantes();

    Optional<Estudiante> getEstudianteById(Long estudianteId);

    Estudiante crearEstudiante(Object o);
}

