package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "estudiantes")
@Node("Estudiantes")
public class Estudiante {
    @Id
    private String id;
    private String nombre;
    private String paisOrigen;
    private List<String> calificacionIds = new ArrayList<>();
    private String institucionActual;

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

