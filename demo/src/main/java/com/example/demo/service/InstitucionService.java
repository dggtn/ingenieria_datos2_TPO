package com.example.demo.service;
import com.example.demo.model.Institucion;
import com.example.demo.model.RequestRegistrarInstitucion;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InstitucionService {
    @Autowired
    private InstitucionNeo4jRepository institucionRepository;


    public Institucion registrarInstitucion(RequestRegistrarInstitucion requestRegistrarInstitucion) {
            Institucion i = new Institucion();
            i.setId(UUID.randomUUID());
            i.setPais(requestRegistrarInstitucion.getPais());
            i.setNombre(requestRegistrarInstitucion.getNombre());
            Institucion guardada = institucionRepository.save(i);
            System.out.println("El ID generado es: " + guardada.getId());
            return guardada;
        }
        }


