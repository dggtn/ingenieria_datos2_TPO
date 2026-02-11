package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Document(collection = "calificaciones")
public class Calificacion {
    @Id
    private String id;
    private String estudianteId;
    private String materiaId;
    private String paisOrigen;
    private String notaOriginal;
    private String auditor;
    private LocalDateTime fechaProcesamiento;

    // RF1: todos los bson de cada sistema (UK,AR,USA,GER)
    private Map<String, Object> detallesOriginales = new HashMap<>();

    // RF2: El mapa de conversiones
    private Map<String, String> conversiones = new HashMap<>();

    public Calificacion() {
    }

    public Calificacion(String id, String notaOriginal, String paisOrigen, String auditor, LocalDateTime fechaProcesamiento) {
        this.id = id;
        this.notaOriginal = notaOriginal;
        this.paisOrigen = paisOrigen;
        this.auditor = auditor;
        this.fechaProcesamiento = fechaProcesamiento;

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

    public String getNotaOriginal() {
        return notaOriginal;
    }

    public void setNotaOriginal(String notaOriginal) {
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

    public Map<String, Object> getDetallesOriginales() {
        return detallesOriginales;
    }

    public void setDetallesOriginales(Map<String, Object> detallesOriginales) {
        this.detallesOriginales = detallesOriginales;
    }

    public Map<String, String> getConversiones() {
        return conversiones;
    }

    public void setConversiones(Map<String, String> conversiones) {
        this.conversiones = conversiones;
    }
}


