package com.example.demo.service;

import com.example.demo.model.Calificacion;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.model.RequestRegistrarMateria;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MateriaService {

    @Autowired
    private MateriaNeo4jRepository materiaRepository;

    public Materia registrarMateria(RequestRegistrarMateria requestRegistrarMateria) {
        Materia m = new Materia();
        m.setNombre(requestRegistrarMateria.getNombre());
        m.getId();

        return materiaRepository.save(m);
    }
}
