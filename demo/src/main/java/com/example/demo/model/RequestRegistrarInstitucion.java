package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RequestRegistrarInstitucion {
    @JsonProperty("padron")
    private String padron;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("pais")
    private String pais;
    @JsonProperty("email")
    private String email;

    public RequestRegistrarInstitucion() {
    }

    public RequestRegistrarInstitucion(String padron, String nombre, String pais, String email) {
        this.padron = padron;
        this.nombre = nombre;
        this.pais = pais;
        this.email = email;
    }

    public String getPadron() {
        return padron;
    }

    public void setPadron(String padron) {
        this.padron = padron;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
