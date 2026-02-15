package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RequestRegistrarCalificacion {
   @JsonProperty("pais")
    private String paisOrigen;
    @JsonProperty("estudiante")
    private String estudiante;
    @JsonProperty("institucion")
    private String institucion;
    @JsonProperty("materia")
    private String materia;
    @JsonProperty("metadatos")
    private Map<String,Object>metadatos;

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Map<String, Object> getMetadatos() {
        return this.metadatos;
    }

    public void setMetadatos(Map<String, Object> metadatos) {
        this.metadatos = metadatos;
    }



}
