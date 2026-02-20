package com.example.demo.model;

public class ReporteProvinciaRanking {
    private String provincia;
    private Double promedioConversionSudafrica;
    private Long cantidadEstudiantes;

    public ReporteProvinciaRanking() {
    }

    public ReporteProvinciaRanking(String provincia, Double promedioConversionSudafrica, Long cantidadEstudiantes) {
        this.provincia = provincia;
        this.promedioConversionSudafrica = promedioConversionSudafrica;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
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
