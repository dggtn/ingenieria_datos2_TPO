package com.example.demo.cassandra.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class Estudiante {

    public Estudiante() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;
    private String apellido;
    private Institucion institucion;
    private Calificacion calificacion;
    private List <Calificacion> trayectoria;
    private String nacionalidad;


    public Estudiante(Long id, String nombre, String apellido, Institucion institucion, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.institucion = institucion;
        this.nacionalidad = nacionalidad;
    }
}

