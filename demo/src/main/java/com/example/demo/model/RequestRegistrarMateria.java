package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestRegistrarMateria {
    @JsonProperty("codigo")
    private String codigo;
    @JsonProperty("nombre")
    private String nombre;

    public RequestRegistrarMateria() {
    }

    public RequestRegistrarMateria(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
