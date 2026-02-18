package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "equivalencias_calificaciones")
public class EquivalenciaCalificacion {
    @Id
    private String id;
    private String versionGlobal;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private Map<String, PaisEquivalencia> paises = new HashMap<>();

    public EquivalenciaCalificacion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionGlobal() {
        return versionGlobal;
    }

    public void setVersionGlobal(String versionGlobal) {
        this.versionGlobal = versionGlobal;
    }

    public LocalDate getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(LocalDate vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public LocalDate getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(LocalDate vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public Map<String, PaisEquivalencia> getPaises() {
        return paises;
    }

    public void setPaises(Map<String, PaisEquivalencia> paises) {
        this.paises = paises != null ? paises : new HashMap<>();
    }

    public static class PaisEquivalencia {
        private Map<String, Double> equivalencias = new HashMap<>();
        private Double umbralMenorQue;
        private Double valorMenorQue;
        private String descripcion;

        public PaisEquivalencia() {
        }

        public PaisEquivalencia(Map<String, Double> equivalencias, Double umbralMenorQue, Double valorMenorQue, String descripcion) {
            this.equivalencias = equivalencias != null ? equivalencias : new HashMap<>();
            this.umbralMenorQue = umbralMenorQue;
            this.valorMenorQue = valorMenorQue;
            this.descripcion = descripcion;
        }

        public Map<String, Double> getEquivalencias() {
            return equivalencias;
        }

        public void setEquivalencias(Map<String, Double> equivalencias) {
            this.equivalencias = equivalencias != null ? equivalencias : new HashMap<>();
        }

        public Double getUmbralMenorQue() {
            return umbralMenorQue;
        }

        public void setUmbralMenorQue(Double umbralMenorQue) {
            this.umbralMenorQue = umbralMenorQue;
        }

        public Double getValorMenorQue() {
            return valorMenorQue;
        }

        public void setValorMenorQue(Double valorMenorQue) {
            this.valorMenorQue = valorMenorQue;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}
