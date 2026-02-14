package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table
public class ReportePromedio {
    @Id
    private String id;
    private Institucion instId;
    private double promedio;
    private LocalDate fecha;

    public ReportePromedio(Institucion instId, double promedio, LocalDate fecha) {
        this.instId = instId;
        this.promedio = promedio;
        this.fecha = fecha;
    }

    public ReportePromedio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Institucion getInstId() {
        return instId;
    }

    public void setInstId(Institucion instId) {
        this.instId = instId;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
