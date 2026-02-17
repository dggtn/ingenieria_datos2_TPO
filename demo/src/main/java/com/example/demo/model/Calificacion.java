package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "calificaciones")
public class Calificacion {
    @Id
    private String id;
    private String estudianteId;
    private String materiaId;
    private String paisOrigen;
    private Double notaOriginal;
    private String auditor;
    private LocalDateTime fechaProcesamiento;

    private Map<String, Object> metadata = new HashMap<>();

    private Double conversiones ;

    public Calificacion() {
    }

    public Calificacion(String id, String estudianteId, String materiaId, String paisOrigen, Double notaOriginal, String auditor, LocalDateTime fechaProcesamiento, Map<String, Object> metadata, Double conversiones) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.materiaId = materiaId;
        this.paisOrigen = paisOrigen;
        this.notaOriginal = notaOriginal;
        this.auditor = auditor;
        this.fechaProcesamiento = fechaProcesamiento;
        this.metadata = metadata;
        this.conversiones =conversiones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(String materiaId) {
        this.materiaId = materiaId;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public Double getNotaOriginal() {
        return notaOriginal;
    }

    public void setNotaOriginal(Double notaOriginal) {
        this.notaOriginal = notaOriginal;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Double getConversiones() {
        return conversiones;
    }

    public void setConversiones(Double conversiones) {
        this.conversiones = conversiones;
    }
}


