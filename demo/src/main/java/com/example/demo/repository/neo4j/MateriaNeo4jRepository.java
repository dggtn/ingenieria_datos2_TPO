package com.example.demo.repository.neo4j;

import com.example.demo.model.Materia;
import com.example.demo.model.MateriaOpcion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaNeo4jRepository extends Neo4jRepository<Materia, String> {
    @Query("MATCH (i:Institucion {id: $idInstitucion})-[:OFRECE]->(m:Materia) " +
            "RETURN DISTINCT m.id AS id, m.nombre AS nombre ORDER BY m.nombre")
    List<MateriaOpcion> listarMateriasPorInstitucion(@Param("idInstitucion") String idInstitucion);

    @Query("""
            MATCH (:Institucion {id: $idInstitucionOrigen, nivelEducativo: 'Universidad'})-[:OFRECE]->(:Materia {id: $idMateriaOrigen})
            RETURN count(*) > 0
            """)
    boolean existeMateriaUniversitaria(@Param("idInstitucionOrigen") String idInstitucionOrigen,
                                       @Param("idMateriaOrigen") String idMateriaOrigen);

    @Query("""
            MATCH (:Institucion {id: $idInstitucionOrigen, nivelEducativo: 'Universidad'})-[:OFRECE]->(m1:Materia {id: $idMateriaOrigen})
            MATCH (:Institucion {id: $idInstitucionDestino, nivelEducativo: 'Universidad'})-[:OFRECE]->(m2:Materia {id: $idMateriaDestino})
            MERGE (m1)-[r:EQUIVALENCIA]->(m2)
            SET r.activa = true,
                r.actualizadoEn = datetime()
            """)
    void registrarEquivalencia(@Param("idInstitucionOrigen") String idInstitucionOrigen,
                               @Param("idMateriaOrigen") String idMateriaOrigen,
                               @Param("idInstitucionDestino") String idInstitucionDestino,
                               @Param("idMateriaDestino") String idMateriaDestino);

}
