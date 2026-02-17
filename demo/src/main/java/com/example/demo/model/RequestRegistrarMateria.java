package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestRegistrarMateria {
    @JsonProperty("nombre")
    private String nombre;

    public RequestRegistrarMateria() {
    }

    public RequestRegistrarMateria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
