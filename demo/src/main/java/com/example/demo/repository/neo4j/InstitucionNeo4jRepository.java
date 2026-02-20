package com.example.demo.repository.neo4j;

import com.example.demo.model.Institucion;
import com.example.demo.model.InstitucionOpcion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstitucionNeo4jRepository extends Neo4jRepository<Institucion, String> {
    Institucion findByNombre(String nombre);

    @Query("MATCH (i:Institucion) " +
            "RETURN i.id AS id, coalesce(i.nombre, i.id) AS nombre, i.pais AS pais " +
            "ORDER BY nombre")
    List<InstitucionOpcion> listarOpciones();
}
