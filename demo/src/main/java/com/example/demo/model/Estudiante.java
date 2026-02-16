package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "estudiantes")
@Node("Estudiantes")
public class Estudiante {
    @PrimaryKey
    @Id
    private UUID id;
    private String nombre;
    private String email;
    private String paisOrigen;
    private List<String> calificacionIds = new ArrayList<>();
    private String institucionActual;
    private Map<String,Object> historial ;

    public Estudiante() {
    }

    public Estudiante(UUID id, String nombre, String email, String paisOrigen, List<String> calificacionIds, String institucionActual, Map<String, Object> historial) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.paisOrigen = paisOrigen;
        this.calificacionIds = calificacionIds;
        this.institucionActual = institucionActual;
        this.historial = historial;
    }

    public Map<String, Object> getHistorial() {
        return historial;
    }

    public void setHistorial(Map<String, Object> historial) {
        this.historial = historial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

