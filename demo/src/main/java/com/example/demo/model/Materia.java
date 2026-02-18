package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "materias")
@Node("Materia")
public class Materia {
    @org.springframework.data.annotation.Id
    @org.springframework.data.neo4j.core.schema.Id
    private String id;
    private String nombre;
    private Integer anioCarrera;

    // Campos para uso en historial academico (compartidos)
    private String sistemaCalificacion;
    private List<String> examenesParciales = new ArrayList<>();
    private List<String> trabajosPracticos = new ArrayList<>();
    private List<String> examenesFinales = new ArrayList<>();
    private Double promedioFinal;
    private LocalDate fechaAprobacionFinal;

    public Materia() {
    }

    public Materia(String id, String nombre, Integer anioCarrera) {
        this.id = id;
        this.nombre = nombre;
        this.anioCarrera = anioCarrera;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioCarrera() {
        return anioCarrera;
    }

    public void setAnioCarrera(Integer anioCarrera) {
        this.anioCarrera = anioCarrera;
    }

    // Alias para compatibilidad con payloads de historial
    public String getMateriaId() {
        return id;
    }

    public void setMateriaId(String materiaId) {
        this.id = materiaId;
    }

    public String getMateriaNombre() {
        return nombre;
    }

    public void setMateriaNombre(String materiaNombre) {
        this.nombre = materiaNombre;
    }

    public Integer getAnioLectivo() {
        return anioCarrera;
    }

    public void setAnioLectivo(Integer anioLectivo) {
        this.anioCarrera = anioLectivo;
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
}
