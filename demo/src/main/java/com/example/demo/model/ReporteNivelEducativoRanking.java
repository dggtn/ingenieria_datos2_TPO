package com.example.demo.model;

public class ReporteNivelEducativoRanking {
    private String nivelEducativo;
    private Double promedioConversionSudafrica;
    private Long cantidadEstudiantes;

    public ReporteNivelEducativoRanking() {
    }

    public ReporteNivelEducativoRanking(String nivelEducativo, Double promedioConversionSudafrica, Long cantidadEstudiantes) {
        this.nivelEducativo = nivelEducativo;
        this.promedioConversionSudafrica = promedioConversionSudafrica;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
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
