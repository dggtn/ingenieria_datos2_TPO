package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "calificaciones")
public class Calificacion {
    @Id
    private String id;
    private String tipoPais;
    private String valorNota;
    private LocalDateTime fecha; // Timestamp
    private String auditor;

    public Calificacion() {
    }

    public Calificacion(String id, LocalDateTime fecha, String auditor) {
        this.id = UUID.randomUUID().toString();
        this.fecha = LocalDateTime.now();
        this.auditor = "auditor";
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoPais() {
        return tipoPais;
    }

    public void setTipoPais(String tipoPais) {
        this.tipoPais = tipoPais;
    }

    public String getValorNota() {
        return valorNota;
    }

    public void setValorNota(String valorNota) {
        this.valorNota = valorNota;
    }
}
