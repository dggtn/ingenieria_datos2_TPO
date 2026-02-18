package com.example.demo.model;

import java.util.UUID;

public class ReportePromedio {
    private UUID  idestudiante;
    private Double promedio;
    private UUID idmateria;
    private UUID idinstitucion;
    private String pais;
    private String nombre ;

    public ReportePromedio(UUID idestudiante, Double promedio, UUID idmateria, UUID idinstitucion, String pais, String nombre) {
        this.idestudiante = idestudiante;
        this.promedio = promedio;
        this.idmateria = idmateria;
        this.idinstitucion = idinstitucion;
        this.pais = pais;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPromedio(Object promedio) {
        if (promedio instanceof Number) {
            this.promedio = ((Number) promedio).doubleValue();
        }
    }
    public UUID getIdestudiante() {
        return idestudiante;
    }

    public void setIdestudiante(UUID idestudiante) {
        this.idestudiante = idestudiante;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public UUID getIdmateria() {
        return idmateria;
    }

    public void setIdmateria(UUID idmateria) {
        this.idmateria = idmateria;
    }

    public UUID getIdinstitucion() {
        return idinstitucion;
    }

    public void setIdinstitucion(UUID idinstitucion) {
        this.idinstitucion = idinstitucion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
