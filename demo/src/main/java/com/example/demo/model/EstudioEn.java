package com.example.demo.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Optional;

@RelationshipProperties
public class EstudioEn {

    @RelationshipId
    private Long id;

    @TargetNode
    private Institucion institucion;

    private String periodo;
    private String nivel;

    public EstudioEn(Institucion institucion, String periodo, String nivel) {
        this.institucion = institucion;
        this.periodo = periodo;
        this.nivel = nivel;
    }
}
