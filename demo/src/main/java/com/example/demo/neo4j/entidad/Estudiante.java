package com.example.demo.neo4j.entidad;

import jakarta.persistence.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
public class Estudiante {
    @Id
    private String pasaporte;
    private String nombre;
    private String paisOrigen;

    public Estudiante(String pasaporte, String nombre, String paisOrigen, List<Institucion> instituciones) {
        this.pasaporte = pasaporte;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.instituciones = instituciones;
    }

    @Relationship(type = "CURSA_EN", direction = Relationship.Direction.OUTGOING)
    private List<Institucion> instituciones;

    @Relationship(type = "TIENE_TRAYECTORIA")
    private TrayectoriaAcademica trayectoria;


}