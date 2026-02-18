package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "estudiantes")
@Node("Estudiante")
public class Estudiante {
    @org.springframework.data.annotation.Id
    @org.springframework.data.neo4j.core.schema.Id
    private String id;

    private String nombre;
    private String idNacional;
    private LocalDate fechaNacimiento;
    private String paisOrigen;
    private String provincia;
    private String institucionActualId;
    private List<Materia> materiasInstitucionActual = new ArrayList<>();
    private List<HistorialInstitucion> historialAcademico = new ArrayList<>();

    public Estudiante() {
    }

    public Estudiante(String id, String nombre, String idNacional, LocalDate fechaNacimiento, String paisOrigen,
                      String provincia,
                      String institucionActualId, List<Materia> materiasInstitucionActual, List<HistorialInstitucion> historialAcademico) {
        this.id = id;
        this.nombre = nombre;
        this.idNacional = idNacional;
        this.fechaNacimiento = fechaNacimiento;
        this.paisOrigen = paisOrigen;
        this.provincia = provincia;
        this.institucionActualId = institucionActualId;
        this.materiasInstitucionActual = materiasInstitucionActual != null ? materiasInstitucionActual : new ArrayList<>();
        this.historialAcademico = historialAcademico != null ? historialAcademico : new ArrayList<>();
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

    public String getIdNacional() {
        return idNacional;
    }

    public void setIdNacional(String idNacional) {
        this.idNacional = idNacional;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getInstitucionActualId() {
        return institucionActualId;
    }

    public void setInstitucionActualId(String institucionActualId) {
        this.institucionActualId = institucionActualId;
    }

    public List<Materia> getMateriasInstitucionActual() {
        return materiasInstitucionActual;
    }

    public void setMateriasInstitucionActual(List<Materia> materiasInstitucionActual) {
        this.materiasInstitucionActual = materiasInstitucionActual != null ? materiasInstitucionActual : new ArrayList<>();
    }

    public List<HistorialInstitucion> getHistorialAcademico() {
        return historialAcademico;
    }

    public void setHistorialAcademico(List<HistorialInstitucion> historialAcademico) {
        this.historialAcademico = historialAcademico != null ? historialAcademico : new ArrayList<>();
    }

    public static class HistorialInstitucion {
        private String institucionId;
        private String institucionNombre;
        private List<Materia> materias = new ArrayList<>();

        public HistorialInstitucion() {
        }

        public HistorialInstitucion(String institucionId, String institucionNombre, List<Materia> materias) {
            this.institucionId = institucionId;
            this.institucionNombre = institucionNombre;
            this.materias = materias != null ? materias : new ArrayList<>();
        }

        public String getInstitucionId() {
            return institucionId;
        }

        public void setInstitucionId(String institucionId) {
            this.institucionId = institucionId;
        }

        public String getInstitucionNombre() {
            return institucionNombre;
        }

        public void setInstitucionNombre(String institucionNombre) {
            this.institucionNombre = institucionNombre;
        }

        public List<Materia> getMaterias() {
            return materias;
        }

        public void setMaterias(List<Materia> materias) {
            this.materias = materias != null ? materias : new ArrayList<>();
        }
    }
}
