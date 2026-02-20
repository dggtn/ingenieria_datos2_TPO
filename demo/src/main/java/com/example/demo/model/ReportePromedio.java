package com.example.demo.model;

public class ReportePromedio {
    private String idestudiante;
    private Double promedio;
    private String idmateria;
    private String idinstitucion;
    private String pais;
    private String nombre ;

    public ReportePromedio(String idestudiante, Double promedio, String idmateria, String idinstitucion, String pais, String nombre) {
        this.idestudiante = idestudiante;
        this.promedio = promedio;
        this.idmateria = idmateria;
        this.idinstitucion = idinstitucion;
        this.pais = pais;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPromedio(Object promedio) {
        if (promedio instanceof Number) {
            this.promedio = ((Number) promedio).doubleValue();
        }
    }
    public String getIdestudiante() {
        return idestudiante;
    }

    public void setIdestudiante(String idestudiante) {
        this.idestudiante = idestudiante;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public String getIdmateria() {
        return idmateria;
    }

    public void setIdmateria(String idmateria) {
        this.idmateria = idmateria;
    }

    public String getIdinstitucion() {
        return idinstitucion;
    }

    public void setIdinstitucion(String idinstitucion) {
        this.idinstitucion = idinstitucion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
