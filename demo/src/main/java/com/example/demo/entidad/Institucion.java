package com.example.demo.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;

public class Institucion<T> {

    private Long id;
    private String nombre;
    private String pais;
    private ArrayList<T> escalas;

    public Institucion(Long id, String nombre, String pais, ArrayList<T> escalas) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.escalas = escalas;
    }
}

