package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestRegistrarMateria {
    @JsonProperty("codigo")
    private String codigo;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("institucionId")
    private String institucionId;

    public RequestRegistrarMateria() {
    }

    public RequestRegistrarMateria(String codigo, String nombre, String institucionId) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.institucionId = institucionId;
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

    public String getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(String institucionId) {
        this.institucionId = institucionId;
    }
}
