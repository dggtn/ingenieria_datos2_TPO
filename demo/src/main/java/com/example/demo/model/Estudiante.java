package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table
@Document(collection = "estudiantes")
@Node("Estudiantes")
public class Estudiante {
    @PrimaryKey
    @Id
    @GeneratedValue
    private UUID id;
    private String nombre;
    private String paisOrigen;
    private List<String> calificacionIds = new ArrayList<>();
    private String institucionActual;

    public Estudiante() {
    }

    public Estudiante(UUID id, String nombre, String paisOrigen, List<String> calificacionIds, String institucionActual) {
        this.id = id;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.calificacionIds = calificacionIds;
        this.institucionActual = institucionActual;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public List<String> getCalificacionIds() {
        return calificacionIds;
    }

    public void setCalificacionIds(List<String> calificacionIds) {
        this.calificacionIds = calificacionIds;
    }

    public String getInstitucionActual() {
        return institucionActual;
    }

    public void setInstitucionActual(String institucionActual) {
        this.institucionActual = institucionActual;
    }
}

