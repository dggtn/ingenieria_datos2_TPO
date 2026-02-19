package com.example.demo.repository.neo4j;

import com.example.demo.model.Institucion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitucionNeo4jRepository extends Neo4jRepository<Institucion, String> {
    Institucion findByNombre(String nombre);

    @Query("MATCH (i:Institucion {id: $id}) DETACH DELETE i")
    void eliminarPorId(@Param("id") String id);

    @Query("MATCH (i:Institucion {id: $idInstitucion})-[r:OFRECE]->(c:Curso {id: $idCurso}) DELETE r")
    void eliminarRelacionInstitucionCurso(@Param("idInstitucion") String idInstitucion, @Param("idCurso") String idCurso);

    @Query("MATCH (c:Curso) WHERE NOT (c)--() DETACH DELETE c")
    void eliminarCursosHuerfanos();
}
