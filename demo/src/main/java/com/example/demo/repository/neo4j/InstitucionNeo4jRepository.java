package com.example.demo.repository.neo4j;

import com.example.demo.model.Institucion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstitucionNeo4jRepository extends Neo4jRepository<Institucion, UUID> {
    Institucion findByNombre(String nombre);
}
