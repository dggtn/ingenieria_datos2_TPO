package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestModificarEstudiante {
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("paisOrigen")
    private String paisOrigen;
    @JsonProperty("institucionActual")
    private String institucionActual;
    @JsonProperty("email")
    private String email;

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
}
