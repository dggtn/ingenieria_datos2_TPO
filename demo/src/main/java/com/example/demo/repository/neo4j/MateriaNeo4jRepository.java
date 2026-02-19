package com.example.demo.repository.neo4j;

import com.example.demo.model.Materia;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface MateriaNeo4jRepository extends Neo4jRepository<Materia, String> {
    @Query("MATCH (m:Materia) WHERE NOT (m)--() DETACH DELETE m")
    void eliminarMateriasHuerfanas();
}
