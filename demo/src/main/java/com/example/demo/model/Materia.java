package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node("Materia")
public class Materia {
    @Id
    @GeneratedValue
    private UUID id;
    private String nombre;


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(UUID uuid) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
