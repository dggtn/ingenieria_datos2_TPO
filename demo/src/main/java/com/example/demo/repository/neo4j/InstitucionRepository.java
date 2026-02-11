package com.example.demo.repository.neo4j;


import com.example.demo.model.Institucion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


    public interface InstitucionRepository extends Neo4jRepository<Institucion, String> {

        Institucion findByNombre(String nombre);
        Institucion findByid(Long id);
    }
