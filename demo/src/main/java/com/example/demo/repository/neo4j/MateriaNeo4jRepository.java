package com.example.demo.repository.neo4j;

import com.example.demo.model.Materia;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MateriaNeo4jRepository extends Neo4jRepository<Materia, String> {
    @Query("MATCH (i:Institucion {id: $idInstitucion})-[:OFRECE]->(m:Materia) " +
            "RETURN DISTINCT m.id AS id, m.nombre AS nombre ORDER BY m.nombre")
    List<Map<String, Object>> listarMateriasPorInstitucion(@Param("idInstitucion") String idInstitucion);
}
