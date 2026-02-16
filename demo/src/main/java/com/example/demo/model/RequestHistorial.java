package com.example.demo.model;

public class RequestHistorial {
    private String estudianteId;
    private String institucionId;
    private String periodo;
    private String nivel;

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getInstitucionId() {
        return institucionId;
    }

    public void setInstitucionId(String institucionId) {
        this.institucionId = institucionId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}
