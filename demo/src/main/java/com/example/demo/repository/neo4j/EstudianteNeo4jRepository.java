package com.example.demo.repository.neo4j;

import com.example.demo.model.Estudiante;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteNeo4jRepository extends Neo4jRepository<Estudiante, String> {
    @Query("MERGE (e:Estudiante {id: $estudianteId}) " +
            "MERGE (i:Institucion {id: $institucionId}) " +
            "MERGE (e)-[r:ASISTIO_A]->(i) " +
            "SET r.esActual = $esActual")
    void registrarHistorial(@Param("estudianteId") String estudianteId,
                            @Param("institucionId") String institucionId,
                            @Param("esActual") boolean esActual);
}
