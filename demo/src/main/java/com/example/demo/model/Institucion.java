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
    private String provincia;
    private String nivelEducativo;
    @Relationship(type = "OFRECE", direction = Relationship.Direction.OUTGOING)
    private List<Curso> curriculum = new ArrayList<>();

    public Institucion() {
    }

    public Institucion(String id, String nombre, String pais, String provincia, String nivelEducativo, List<Curso> curriculum) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.provincia = provincia;
        this.nivelEducativo = nivelEducativo;
        this.curriculum = curriculum;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public List<Curso> getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(List<Curso> curriculum) {
        this.curriculum = curriculum;
    }

    @Node("Curso")
    public static class Curso {
        @Id
        private String id;
        private String nombre;

        public Curso() {
        }

        public Curso(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
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
    }
}
