package com.example.demo.entidad.dto;

import java.util.ArrayList;

public class InstitucionDTO<T> {
    private Long id;
    private String nombre;
    private String pais;
    private ArrayList<T> escalas;

    public InstitucionDTO(Long id, String nombre, String pais, ArrayList<T> escalas) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.escalas = escalas;
    }
}
