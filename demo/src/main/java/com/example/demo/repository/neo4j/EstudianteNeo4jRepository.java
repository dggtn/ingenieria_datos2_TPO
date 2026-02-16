package com.example.demo.repository.neo4j;
import com.example.demo.model.Estudiante;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface EstudianteNeo4jRepository extends Neo4jRepository<Estudiante, UUID>{
    @Query("MATCH (e:Estudiante {id: $estudianteId})-[r:ESTUDIO_EN]->(i:Institucion) " +
            "RETURN i.id AS institucionId, i.nombre AS nombre_institucion")
    void registrarHistorial(String estudianteId, String institucionId);



    @Query("MATCH (e:Estudiante {id: $estudianteId}) "  )
    List<Map<String, Object>> obtenerHistorialAcademico(String estudianteId);

}
