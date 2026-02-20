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
            "SET e.institucionActual = $idInstitucion\n" +
            "WITH e\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (e)-[r:ESTUDIO_EN]->(i)\n" +
            "SET r.periodo = CASE WHEN $periodo IS NULL OR $periodo = '' THEN r.periodo ELSE $periodo END")
    void registrarDondeEstudio(String idEstudiante, String idInstitucion, String periodo);

    @Query ("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (m:Materia {id: $idMateria})\n" +
            "MERGE (e)-[r:CURSO]->(m)\n" +
            "SET r.periodo = CASE WHEN $periodo IS NULL OR $periodo = '' THEN r.periodo ELSE $periodo END,\n" +
            "    r.nivel = CASE WHEN $nivel IS NULL OR $nivel = '' THEN r.nivel ELSE $nivel END,\n" +
            "    r.resultado=$resultado,\n" +
            "    r.notaOriginal = $notaOriginal")
    void registrarCursada(String idEstudiante,String idMateria,Double resultado,String periodo,String nivel,String notaOriginal);

    @Query ( "MATCH (e:Estudiante)-[curso:CURSO]->(m:Materia)\n" +
            "RETURN e.paisOrigen AS pais, \n" +
            "       avg(curso.resultado) AS promedio,\n" +
            "       'PAIS' AS tipo")
    List<ReportePromedio> calcularpromedioPorPais();
    @Query ( "MATCH (e:Estudiante)-[curso:CURSO]->(m:Materia)\n" +
            "MATCH (e)-[:ESTUDIO_EN]->(i:Institucion)\n" +
            "RETURN i.nombre AS nombre, \n" +
            "       avg(curso.resultado) AS promedio,\n" +
            "       'INSTITUTO' AS tipo")
    List<ReportePromedio> calcularPromedioPorInstitucion();

    @Query("MATCH (e:Estudiante {id:$idEstudiante})-[r:CURSO]->(m:Materia) " +
            "OPTIONAL MATCH (i:Institucion)-[:OFRECE]->(m) " +
            "WITH m, r, head(collect(i.nombre)) AS institucionNombre " +
            "RETURN m.id AS materiaId, m.nombre AS materia, coalesce(institucionNombre, '-') AS institucion, coalesce(r.notaOriginal, toString(r.resultado)) AS notaOriginal")
    List<ReporteAcademico> obtenerDetalleAcademicoPorInstitucion(@Param("idEstudiante") String idEstudiante);

    @Query("MATCH (e:Estudiante) RETURN e.id AS id, e.nombre AS nombre ORDER BY e.nombre")
    List<Map<String, Object>> listarEstudiantes();

    @Query("MATCH (e:Estudiante {id: $idEstudiante})-[:ESTUDIO_EN]->(i:Institucion) " +
            "RETURN DISTINCT i.id AS id, coalesce(i.nombre, i.id) AS nombre ORDER BY nombre")
    List<Map<String, Object>> listarInstitucionesPorEstudiante(@Param("idEstudiante") String idEstudiante);
}

