package com.example.demo.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Table("calificaciones")
public class CalificacionCassandra {
    @PrimaryKey
    private String eventoId;

    @Column
    private String operacion;

    @Column
    private LocalDateTime fechaEvento;

    @Column
    private String calificacionId;

    @Column
    private String estudianteId;
    @Column
    private String estudianteNombre;
    @Column
    private String estudianteEmail;
    @Column
    private String estudiantePaisOrigen;
    @Column
    private String estudianteInstitucionActual;
    @Column
    private String estudianteHistorialJson;
    @Column
    private String estudianteHistorialAcademicoJson;
    @Column
    private String estudianteMateriasJson;

    @Column
    private String institucionId;
    @Column
    private String institucionNombre;
    @Column
    private String institucionPais;
    @Column
    private String institucionCurriculumJson;

    @Column
    private String notaPaisOrigen;
    @Column
    private String notaMateriaId;
    @Column
    private String notaOriginal;
    @Column
    private Double notaOriginalNumerica;
    @Column
    private Double notaConversionSudafrica;
    @Column
    private String notaAuditor;
    @Column
    private LocalDateTime notaFechaProcesamiento;
    @Column
    private String notaMetadataJson;
    @Column
    private String notaSnapshotJson;

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getCalificacionId() {
        return calificacionId;
    }

    public void setCalificacionId(String calificacionId) {
        this.calificacionId = calificacionId;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public String getEstudianteEmail() {
        return estudianteEmail;
    }

    public void setEstudianteEmail(String estudianteEmail) {
        this.estudianteEmail = estudianteEmail;
    }

    public String getEstudiantePaisOrigen() {
        return estudiantePaisOrigen;
    }

    public void setEstudiantePaisOrigen(String estudiantePaisOrigen) {
        this.estudiantePaisOrigen = estudiantePaisOrigen;
    }

    public String getEstudianteInstitucionActual() {
        return estudianteInstitucionActual;
    }

    public void setEstudianteInstitucionActual(String estudianteInstitucionActual) {
        this.estudianteInstitucionActual = estudianteInstitucionActual;
    }

    public String getEstudianteHistorialJson() {
        return estudianteHistorialJson;
    }

    public void setEstudianteHistorialJson(String estudianteHistorialJson) {
        this.estudianteHistorialJson = estudianteHistorialJson;
    }

    public String getEstudianteHistorialAcademicoJson() {
        return estudianteHistorialAcademicoJson;
    }

    public void setEstudianteHistorialAcademicoJson(String estudianteHistorialAcademicoJson) {
        this.estudianteHistorialAcademicoJson = estudianteHistorialAcademicoJson;
    }

    public String getEstudianteMateriasJson() {
        return estudianteMateriasJson;
    }

    public void setEstudianteMateriasJson(String estudianteMateriasJson) {
        this.estudianteMateriasJson = estudianteMateriasJson;
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

    public String getInstitucionPais() {
        return institucionPais;
    }

    public void setInstitucionPais(String institucionPais) {
        this.institucionPais = institucionPais;
    }

    public String getInstitucionCurriculumJson() {
        return institucionCurriculumJson;
    }

    public void setInstitucionCurriculumJson(String institucionCurriculumJson) {
        this.institucionCurriculumJson = institucionCurriculumJson;
    }

    public String getNotaPaisOrigen() {
        return notaPaisOrigen;
    }

    public void setNotaPaisOrigen(String notaPaisOrigen) {
        this.notaPaisOrigen = notaPaisOrigen;
    }

    public String getNotaMateriaId() {
        return notaMateriaId;
    }

    public void setNotaMateriaId(String notaMateriaId) {
        this.notaMateriaId = notaMateriaId;
    }

    public String getNotaOriginal() {
        return notaOriginal;
    }

    public void setNotaOriginal(String notaOriginal) {
        this.notaOriginal = notaOriginal;
    }

    public Double getNotaOriginalNumerica() {
        return notaOriginalNumerica;
    }

    public void setNotaOriginalNumerica(Double notaOriginalNumerica) {
        this.notaOriginalNumerica = notaOriginalNumerica;
    }

    public Double getNotaConversionSudafrica() {
        return notaConversionSudafrica;
    }

    public void setNotaConversionSudafrica(Double notaConversionSudafrica) {
        this.notaConversionSudafrica = notaConversionSudafrica;
    }

    public String getNotaAuditor() {
        return notaAuditor;
    }

    public void setNotaAuditor(String notaAuditor) {
        this.notaAuditor = notaAuditor;
    }

    public LocalDateTime getNotaFechaProcesamiento() {
        return notaFechaProcesamiento;
    }

    public void setNotaFechaProcesamiento(LocalDateTime notaFechaProcesamiento) {
        this.notaFechaProcesamiento = notaFechaProcesamiento;
    }

    public String getNotaMetadataJson() {
        return notaMetadataJson;
    }

    public void setNotaMetadataJson(String notaMetadataJson) {
        this.notaMetadataJson = notaMetadataJson;
    }

    public String getNotaSnapshotJson() {
        return notaSnapshotJson;
    }

    public void setNotaSnapshotJson(String notaSnapshotJson) {
        this.notaSnapshotJson = notaSnapshotJson;
    }
}
