package com.example.demo.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.*;

@Document("Estudiante")
@Node("Estudiante")
public class    Estudiante {
    @PrimaryKey
    @Id
    private String id;
    private String nombre;
    private String email;
    private String paisOrigen;
    @Relationship(type = "INSTITUCION_ACTUAL", direction = Relationship.Direction.OUTGOING)
    private Institucion institucionActual;

    @Relationship(type = "ESTUDIO_EN", direction = Relationship.Direction.OUTGOING)
    private List<EstudioEn> historialAcademico = new ArrayList<>();
    private List<Map<String,Object>> historial ;

    @Relationship(type = "CURSO", direction = Relationship.Direction.OUTGOING)
    private List<CursoMateria> materias = new LinkedList<>();

    public Estudiante() {
    }

    public Estudiante(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void agregarEstudio(Institucion inst, List<Materia> historialMaterias) {
        this.historialAcademico.add(new EstudioEn(inst, historialMaterias));
    }

    public Estudiante(String id, String nombre, String email, String paisOrigen, Institucion institucionActual, List<EstudioEn> historialAcademico, List<Map<String, Object>> historial) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.paisOrigen = paisOrigen;
        this.institucionActual = institucionActual;
        this.historialAcademico = historialAcademico;
        this.historial = historial;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public Institucion getInstitucionActual() {
        return institucionActual;
    }

    public void setInstitucionActual(Institucion institucionActual) {
        this.institucionActual = institucionActual;
    }

    public List<EstudioEn> getHistorialAcademico() {
        return historialAcademico;
    }

    public void setHistorialAcademico(List<EstudioEn> historialAcademico) {
        this.historialAcademico = historialAcademico;
    }

    public List<Map<String, Object>> getHistorial() {
        return historial;
    }

    public void setHistorial(List<Map<String, Object>> historial) {
        this.historial = historial;
    }

    public void curso(CursoMateria relacion) {
        materias.add(relacion);
    }

    public List<CursoMateria> getMaterias() {
        return materias;
    }

    public void setMaterias(List<CursoMateria> materias) {
        this.materias = materias;
    }
}

