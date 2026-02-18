package com.example.demo.model;

public class ReporteAcademico {
    private String materia;
    private Double promedio;

    public ReporteAcademico() {}

    public ReporteAcademico(String nombre, Double promedio) {
        this.materia = nombre;
        this.promedio = promedio;
    }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public Double getPromedio() { return promedio; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }
}

