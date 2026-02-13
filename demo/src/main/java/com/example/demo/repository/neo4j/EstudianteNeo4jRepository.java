package com.example.demo.repository.neo4j;
import com.example.demo.model.Estudiante;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public interface EstudianteNeo4jRepository extends Neo4jRepository<Estudiante, String> {

    @Query("MERGE (e:Estudiante {id: $estudianteId}) " +
            "MERGE (i:Institucion {id: $institucionId}) " +
            "MERGE (e)-[r:ESTUDIO_EN]->(i) " +
            "SET r.pais = $pais, r.nombre = $nombre , i.nombre = $nombre " )
    void registrarHistorial(String estudianteId, String institucionId, String pais, String nombre);

    @Query("MATCH (e:Estudiante {id: $estudianteId})-[r:ESTUDIO_EN]->(i:Institucion) " +
            "RETURN i.id AS institucionId, i.nombre AS nombre_institucion, r.pais AS pais_relacion, r.nombre AS titulo")
    List<Map<String, Object>> obtenerHistorialAcademico(String estudianteId);
}
