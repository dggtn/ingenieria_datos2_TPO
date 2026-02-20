package com.example.demo.model;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class CursoMateria {
    @RelationshipId
    @GeneratedValue
    private Long id;

    @TargetNode
    private Materia materia;

    private double promedio;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public double getPromedio() {
        return promedio;
    }
}
