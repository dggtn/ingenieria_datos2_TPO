package com.example.demo.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class EstudioEn {

    @RelationshipId
    private Long id;

    @TargetNode
    private Institucion institucion;

    private String periodo; // Ejemplo: "2012-2018"
    private String nivel;   // Ejemplo: "primaria"

    public EstudioEn(Institucion institucion, String periodo, String nivel) {
        this.institucion = institucion;
        this.periodo = periodo;
        this.nivel = nivel;
    }
}
