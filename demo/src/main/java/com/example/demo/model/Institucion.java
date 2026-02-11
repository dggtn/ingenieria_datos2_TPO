package com.example.demo.model;

import org.springframework.data.annotation.Id;

public class Institucion {
    @Id
    private String id;
    private String nombre;
    private String pais;

    public Institucion(String id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }
}
