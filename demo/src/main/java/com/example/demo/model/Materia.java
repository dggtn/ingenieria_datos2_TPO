package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Materia")
public class Materia {
    @Id
    private String id;

    private String nombre;
    private String pais;
    private String calificacion;

}
