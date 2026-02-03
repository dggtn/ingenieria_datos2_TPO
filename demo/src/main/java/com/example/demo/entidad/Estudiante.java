package com.example.demo.entidad;

import jakarta.persistence.*;
import lombok.*;

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

    public Estudiante(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}

