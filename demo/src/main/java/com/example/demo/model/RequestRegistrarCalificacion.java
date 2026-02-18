package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestRegistrarCalificacion {
   @JsonProperty("pais")
    private String paisOrigen;
    @JsonProperty("estudiante")
    private String estudiante;
    @JsonProperty("institucion")
    private String institucion;
    @JsonProperty("materia")
    private String materia;

    // Estructura alineada a Materia
    @JsonProperty("anioLectivo")
    private Integer anioLectivo;
    @JsonProperty("sistemaCalificacion")
    private String sistemaCalificacion;
    @JsonProperty("examenesParciales")
    private List<String> examenesParciales = new ArrayList<>();
    @JsonProperty("trabajosPracticos")
    private List<String> trabajosPracticos = new ArrayList<>();
    @JsonProperty("examenesFinales")
    private List<String> examenesFinales = new ArrayList<>();
    @JsonProperty("promedioFinal")
    private Double promedioFinal;
    @JsonProperty("fechaAprobacionFinal")
    private LocalDate fechaAprobacionFinal;

    // Backward compatibility
    @JsonProperty("metadatos")
    private Map<String,Object>metadatos;

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Integer getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(Integer anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public String getSistemaCalificacion() {
        return sistemaCalificacion;
    }

    public void setSistemaCalificacion(String sistemaCalificacion) {
        this.sistemaCalificacion = sistemaCalificacion;
    }

    public List<String> getExamenesParciales() {
        return examenesParciales;
    }

    public void setExamenesParciales(List<String> examenesParciales) {
        this.examenesParciales = examenesParciales != null ? examenesParciales : new ArrayList<>();
    }

    public List<String> getTrabajosPracticos() {
        return trabajosPracticos;
    }

    public void setTrabajosPracticos(List<String> trabajosPracticos) {
        this.trabajosPracticos = trabajosPracticos != null ? trabajosPracticos : new ArrayList<>();
    }

    public List<String> getExamenesFinales() {
        return examenesFinales;
    }

    public void setExamenesFinales(List<String> examenesFinales) {
        this.examenesFinales = examenesFinales != null ? examenesFinales : new ArrayList<>();
    }

    public Double getPromedioFinal() {
        return promedioFinal;
    }

    public void setPromedioFinal(Double promedioFinal) {
        this.promedioFinal = promedioFinal;
    }

    public LocalDate getFechaAprobacionFinal() {
        return fechaAprobacionFinal;
    }

    public void setFechaAprobacionFinal(LocalDate fechaAprobacionFinal) {
        this.fechaAprobacionFinal = fechaAprobacionFinal;
    }

    public Map<String, Object> getMetadatos() {
        return this.metadatos;
    }

    public void setMetadatos(Map<String, Object> metadatos) {
        this.metadatos = metadatos;
    }
}


