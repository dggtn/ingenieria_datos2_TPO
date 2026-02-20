package com.example.demo.model;

public class ReporteAcademico {
    private String materia;
    private String notaOriginal;

    public ReporteAcademico() {}

    public ReporteAcademico(String nombre, String notaOriginal) {
        this.materia = nombre;
        this.notaOriginal = notaOriginal;
    }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public String getNotaOriginal() { return notaOriginal; }
    public void setNotaOriginal(String notaOriginal) { this.notaOriginal = notaOriginal; }
}

