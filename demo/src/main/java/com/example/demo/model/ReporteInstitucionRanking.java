package com.example.demo.model;

public class ReporteInstitucionRanking {
    private String institucionId;
    private String institucionNombre;
    private Double promedioConversionSudafrica;
    private Long cantidadEstudiantes;

    public ReporteInstitucionRanking() {
    }

    public ReporteInstitucionRanking(String institucionId, String institucionNombre, Double promedioConversionSudafrica, Long cantidadEstudiantes) {
        this.institucionId = institucionId;
        this.institucionNombre = institucionNombre;
        this.promedioConversionSudafrica = promedioConversionSudafrica;
        this.cantidadEstudiantes = cantidadEstudiantes;
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

    public Double getPromedioConversionSudafrica() {
        return promedioConversionSudafrica;
    }

    public void setPromedioConversionSudafrica(Double promedioConversionSudafrica) {
        this.promedioConversionSudafrica = promedioConversionSudafrica;
    }

    public Long getCantidadEstudiantes() {
        return cantidadEstudiantes;
    }

    public void setCantidadEstudiantes(Long cantidadEstudiantes) {
        this.cantidadEstudiantes = cantidadEstudiantes;
    }
}
