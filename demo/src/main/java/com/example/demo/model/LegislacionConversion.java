package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "legislaciones_conversion")
public class LegislacionConversion {
    @Id
    private String id;
    private int version;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private Map<String, Double> ukNotas = new LinkedHashMap<>();
    private Map<Integer, Double> argentinaNotas = new LinkedHashMap<>();
    private Map<String, Double> usaSemester = new LinkedHashMap<>();
    private List<UmbralGpa> usaGpa = new ArrayList<>();
    private double alemaniaBase = 5.0;
    private double alemaniaFactor = 25.0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public Map<String, Double> getUkNotas() {
        return ukNotas;
    }

    public void setUkNotas(Map<String, Double> ukNotas) {
        this.ukNotas = ukNotas;
    }

    public Map<Integer, Double> getArgentinaNotas() {
        return argentinaNotas;
    }

    public void setArgentinaNotas(Map<Integer, Double> argentinaNotas) {
        this.argentinaNotas = argentinaNotas;
    }

    public Map<String, Double> getUsaSemester() {
        return usaSemester;
    }

    public void setUsaSemester(Map<String, Double> usaSemester) {
        this.usaSemester = usaSemester;
    }

    public List<UmbralGpa> getUsaGpa() {
        return usaGpa;
    }

    public void setUsaGpa(List<UmbralGpa> usaGpa) {
        this.usaGpa = usaGpa;
    }

    public double getAlemaniaBase() {
        return alemaniaBase;
    }

    public void setAlemaniaBase(double alemaniaBase) {
        this.alemaniaBase = alemaniaBase;
    }

    public double getAlemaniaFactor() {
        return alemaniaFactor;
    }

    public void setAlemaniaFactor(double alemaniaFactor) {
        this.alemaniaFactor = alemaniaFactor;
    }

    public static class UmbralGpa {
        private double min;
        private double equivalencia;

        public UmbralGpa() {
        }

        public UmbralGpa(double min, double equivalencia) {
            this.min = min;
            this.equivalencia = equivalencia;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getEquivalencia() {
            return equivalencia;
        }

        public void setEquivalencia(double equivalencia) {
            this.equivalencia = equivalencia;
        }
    }
}
