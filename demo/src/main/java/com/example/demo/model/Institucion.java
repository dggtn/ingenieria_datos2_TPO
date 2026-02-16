package com.example.demo.model;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Map;
@Node("Institucion")
public class Institucion {
    @Id
    private String id;
    private String nombre;
    private String pais;
    private String sistema;
    private String provincia;
    private Map<String, Object> metadatos;
    public Institucion(String id, String nombre, String pais, String sistema, String provincia) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.sistema = sistema;
        this.provincia = provincia;
    }

    public Institucion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Map<String, Object> getMetadatos() {
        return metadatos;
    }

    public void setMetadatos(Map<String, Object> metadatos) {
        this.metadatos = metadatos;
    }
}
