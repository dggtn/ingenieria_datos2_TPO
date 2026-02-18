package com.example.demo.repository.neo4j;
import com.example.demo.model.Estudiante;
import com.example.demo.model.ReportePromedio;
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

    @Query ("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (m:Materia {id: $idMateria})\n" +
            "MERGE (e)-[r:CURSO]->(m)\n" +
            "SET r.periodo = $periodo,r.nivel = $nivel,r.resultado=$resultado")
    void registrarCursada(UUID idEstudiante,UUID idMateria,Double resultado,String periodo,String nivel);
    @Query ( "MATCH (m:Materia {id: $idMateria})\n" +
            "WITH m\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (m)-[r:SE_DICTA_EN]->(i)\n" +
            "SET r.nivel = $nivel")
    void registrarDondeDictaMateria(UUID idInstitucion,UUID idMateria,String periodo,String nivel);

    @Query ( "MATCH (e:Estudiante)-[curso:CURSO]->(m:Materia)\n" +
            "RETURN e.paisOrigen AS pais, \n" +
            "       avg(curso.promedio) AS promedio,\n" +
            "       'PAIS' AS tipo")
    List<ReportePromedio> calcularpromedioPorPais();
    @Query ( "MATCH (e:Estudiante)-[curso:CURSO]->(m:Materia)\n" +
            "MATCH (e)-[:ESTUDIO_EN]->(i:Institucion)\n" +
            "RETURN i.nombre AS nombre, \n" +
            "       avg(curso.promedio) AS promedio,\n" +
            "       'INSTITUTO' AS tipo")
    List<ReportePromedio> calcularPromedioPorInstitucion();

    @Query("MATCH (e:Estudiante {id: $idEstudiante})-[r:CURSO]->(m:Materia)-[:SE_DICTA_EN]->(i:Institucion) " +
            "WITH i.nombre AS inst, m.nombre AS mat, r.resultado AS res, r.periodo AS per " +
            "RETURN inst AS institucion, collect([mat, toString(res), per]) AS materias")
    List<Map<String, Object>> obtenerDetalleAcademicoPorInstitucion(String idEstudiante);

}

