package com.example.demo.repository.neo4j;

import com.example.demo.model.Estudiante;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MatreriaNeo4jRepository extends Neo4jRepository<Estudiante, UUID> {
}
