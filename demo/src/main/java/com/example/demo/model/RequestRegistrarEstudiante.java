package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
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
    private List<Map<String,Object>> historial;

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

    public List<Map<String, Object>> getHistorial() {
        return historial;
    }

//    public void setHistorial(Map<String, Object> historial) {
//        this.historial = historial;
//    }
}
