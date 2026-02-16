package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RequestRegistrarEstudiante {
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("paisOrigen")
    private String paisOrigen;
    @JsonProperty("institucionActual")
    private String institucionActual;
    @JsonProperty("email")
    private String email;
    @JsonProperty("metadatos")
    private Map<String,Object> metadatos;

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

    public String getInstitucionActual() {
        return institucionActual;
    }

    public void setInstitucionActual(String institucionActual) {
        this.institucionActual = institucionActual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getMetadatos() {
        return metadatos;
    }

    public void setMetadatos(Map<String, Object> metadatos) {
        this.metadatos = metadatos;
    }
}
