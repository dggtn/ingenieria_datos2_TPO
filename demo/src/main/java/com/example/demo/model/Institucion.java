package com.example.demo.model;

import org.springframework.data.annotation.Id;

public class Institucion {
    @Id
    private String id;

    private String nombre;
    private Pais pais;

    public Institucion(String id, String nombre, Pais pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }
}
