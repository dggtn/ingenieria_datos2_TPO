package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Document("Institucion")
@Node("Institucion")
public class Institucion {
    @PrimaryKey
    @Id
    private String id;
    private String nombre;
    private String pais;
    @Relationship(type = "OFRECE", direction = Relationship.Direction.OUTGOING)
    private List<Materia> curriculum = new ArrayList<>();

    public Institucion() {
    }

    public Institucion(String id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Materia> getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(List<Materia> curriculum) {
        this.curriculum = curriculum;
    }
}
