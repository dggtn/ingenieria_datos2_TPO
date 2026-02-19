package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Node("Materia")
public class Materia {
    @Id
    private String id;
    private String nombre;
    private LocalDate fechaFinalizacion;
    private Double notaFinal;
    private List<Parcial> notasParciales = new ArrayList<>();


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(Double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public List<Parcial> getNotasParciales() {
        return notasParciales;
    }

    public void setNotasParciales(List<Parcial> notasParciales) {
        this.notasParciales = notasParciales;
    }

    public static class Parcial {
        private String nombre;
        private Double nota;

        public Parcial() {
        }

        public Parcial(String nombre, Double nota) {
            this.nombre = nombre;
            this.nota = nota;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getNota() {
            return nota;
        }

        public void setNota(Double nota) {
            this.nota = nota;
        }
    }
}
