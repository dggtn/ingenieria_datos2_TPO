package com.example.demo.model;

import lombok.Setter;
import org.springframework.data.annotation.Id;

public class Institucion {
    @Id
    private String id;
    @Setter
    private String nombre;
    @Setter
    private String pais;

    public Institucion(String id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    public String getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

}
