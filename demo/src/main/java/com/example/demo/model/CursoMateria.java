package com.example.demo.model;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDate;

@RelationshipProperties
public class CursoMateria {
    @RelationshipId
    @GeneratedValue
    private Long id;

    @TargetNode
    private Materia materia;

    private double promedio;
    private Double notaFinal;
    private LocalDate fechaRendida;

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

    public Double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(Double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public LocalDate getFechaRendida() {
        return fechaRendida;
    }

    public void setFechaRendida(LocalDate fechaRendida) {
        this.fechaRendida = fechaRendida;
    }
}
