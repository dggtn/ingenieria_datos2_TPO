package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestModificarCurso {
    @JsonProperty("nombre")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
