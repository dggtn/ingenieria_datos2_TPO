package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestModificarInstitucion {
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("pais")
    private String pais;
    @JsonProperty("provincia")
    private String provincia;
    @JsonProperty("nivelEducativo")
    private String nivelEducativo;

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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }
}
