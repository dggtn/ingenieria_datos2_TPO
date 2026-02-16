package com.example.demo.repository.neo4j;
import com.example.demo.model.Estudiante;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface EstudianteNeo4jRepository extends Neo4jRepository<Estudiante, UUID> {

    @Query("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (e)-[r:ESTUDIO_EN]->(i)\n" +
            "SET r.periodo = $periodo")
    void registrarDondeEstudio(UUID idEstudiante, UUID idInstitucion, String periodo);

    @Query("MATCH (e:Estudiante {id: $estudianteId}), (i:Institucion {id: $institucionId}) ,(m:Materia {id: $materiaId})" +
            "MERGE (e)-[r:ESTUDIO_EN]->(i)-[r:ESTUDIO]->(m) " +
            "SET r.periodo = $periodo, r.nivel = $nivel")
    void registrarRelacionHistorial(UUID estudianteId, UUID institucionId, String periodo, String nivel);

    @Query("MATCH (e:Estudiante {id: $estudianteId})-[r:ESTUDIO_EN]->(i:Institucion) " +
            "RETURN i.nombre AS institucion, r.periodo AS periodo, r.nivel AS nivel")
    List<Map<String, Object>> obtenerHistorialAcademico(UUID estudianteId);

    @Query("MATCH (e:Estudiante)-[CURSO]->(m:Materia), " +
            "(e)-[:ESTUDIO_EN]->(i:Institucion) " +
            "RETURN e.nombre AS estudiante, i.nombre AS institucion, " +
            "e.pais AS pais, avg(c.nota) AS promedio")
    List<Map<String, Object>> obtenerEstadisticasGlobales();
}

//e ESTUDIO_EN i
// e CURSO m CON_NOTA r
//m DICTA_EN i
