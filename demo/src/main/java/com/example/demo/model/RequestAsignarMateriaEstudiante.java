package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class RequestAsignarMateriaEstudiante {
    @JsonProperty("codigo")
    private String codigo;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("fechaFinalizacion")
    private LocalDate fechaFinalizacion;
    @JsonProperty("notaFinal")
    private Double notaFinal;
    @JsonProperty("notasParciales")
    private List<Materia.Parcial> notasParciales;
    @JsonProperty("fechaRendida")
    private LocalDate fechaRendida;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public List<Materia.Parcial> getNotasParciales() {
        return notasParciales;
    }

    public void setNotasParciales(List<Materia.Parcial> notasParciales) {
        this.notasParciales = notasParciales;
    }

    public LocalDate getFechaRendida() {
        return fechaRendida;
    }

    public void setFechaRendida(LocalDate fechaRendida) {
        this.fechaRendida = fechaRendida;
    }
}
