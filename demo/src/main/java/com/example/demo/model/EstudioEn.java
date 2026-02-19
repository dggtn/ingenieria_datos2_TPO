package com.example.demo.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.ArrayList;
import java.util.List;


@RelationshipProperties
public class EstudioEn {

    @RelationshipId
    @GeneratedValue
    private Long id;

    @TargetNode
    private Institucion institucion;

    private List<Materia> historialMaterias = new ArrayList<>();

    public EstudioEn() {
    }

    public EstudioEn(Institucion institucion, List<Materia> historialMaterias) {
        this.institucion = institucion;
        this.historialMaterias = historialMaterias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public List<Materia> getHistorialMaterias() {
        return historialMaterias;
    }

    public void setHistorialMaterias(List<Materia> historialMaterias) {
        this.historialMaterias = historialMaterias;
    }
}
