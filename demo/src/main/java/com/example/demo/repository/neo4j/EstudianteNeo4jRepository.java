package com.example.demo.repository.neo4j;
import com.example.demo.model.Estudiante;
import com.example.demo.model.ReporteAcademico;
import com.example.demo.model.ReportePromedio;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EstudianteNeo4jRepository extends Neo4jRepository<Estudiante, String> {

    @Query("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (e)-[r:ESTUDIO_EN]->(i)\n" +
            "SET r.periodo = $periodo")
    void registrarDondeEstudio(String idEstudiante, String idInstitucion, String periodo);

    @Query ("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (m:Materia {id: $idMateria})\n" +
            "MERGE (e)-[r:CURSO]->(m)\n" +
            "SET r.periodo = $periodo,r.nivel = $nivel,r.resultado=$resultado")
    void registrarCursada(String idEstudiante,String idMateria,Double resultado,String periodo,String nivel);
    @Query ( "MATCH (m:Materia {id: $idMateria})\n" +
            "WITH m\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (m)-[r:SE_DICTA_EN]->(i)\n" +
            "SET r.nivel = $nivel")
    void registrarDondeDictaMateria(String idInstitucion,String idMateria,String periodo,String nivel);

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

    @Query("MATCH (e:Estudiante)-[r:CURSO]->(m:Materia) WHERE e.id=$idEstudiante " +
            "RETURN m.nombre AS materia, r.promedio AS promedio")
    List<ReporteAcademico> obtenerDetalleAcademicoPorInstitucion(@Param("idEstudiante") String idEstudiante);
}

