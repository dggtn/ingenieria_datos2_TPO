package com.example.demo.model;

public class ReporteAcademico {
    private String materiaId;
    private String materia;
    private String institucion;
    private String notaOriginal;
    private Double notaConvertidaSudafrica;

    public ReporteAcademico() {}

    public ReporteAcademico(String materiaId, String nombre, String institucion, String notaOriginal, Double notaConvertidaSudafrica) {
        this.materiaId = materiaId;
        this.materia = nombre;
        this.institucion = institucion;
        this.notaOriginal = notaOriginal;
        this.notaConvertidaSudafrica = notaConvertidaSudafrica;
    }

    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
    public String getNotaOriginal() { return notaOriginal; }
    public void setNotaOriginal(String notaOriginal) { this.notaOriginal = notaOriginal; }
    public Double getNotaConvertidaSudafrica() { return notaConvertidaSudafrica; }
    public void setNotaConvertidaSudafrica(Double notaConvertidaSudafrica) { this.notaConvertidaSudafrica = notaConvertidaSudafrica; }
}

