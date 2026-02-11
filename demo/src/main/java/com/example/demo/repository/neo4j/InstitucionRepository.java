package com.example.demo.repository.neo4j;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitucionRepository {
    @Query("MATCH (i:Institucion {institucion: $nombre, pais: $pais})-[:EQUIVALE_A]->(res) RETURN res.valor")
    String encontrarEquivalencia(String nota, String pais);
}
