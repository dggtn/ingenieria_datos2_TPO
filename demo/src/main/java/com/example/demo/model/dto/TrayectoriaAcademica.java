package com.example.demo.model.dto;

import com.example.demo.model.Calificacion;

import java.util.List;

public class TrayectoriaAcademica {
public String nombreEstudiante;
public String paisOrigen;
public String nombreInstitucion;
public List<Calificacion> calificacionesRevalidadas;

    public TrayectoriaAcademica(String nombreInstitucion, String paisOrigen, List<Calificacion> calificacionesRevalidadas) {
        this.nombreInstitucion = nombreInstitucion;
        this.paisOrigen = paisOrigen;
        this.calificacionesRevalidadas = calificacionesRevalidadas;
    }
}
