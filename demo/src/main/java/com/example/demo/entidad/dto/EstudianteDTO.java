package com.example.demo.entidad.dto;

import com.example.demo.entidad.Calificacion;
import com.example.demo.entidad.Institucion;
import lombok.*;

import java.util.List;


@Data

public class EstudianteDTO {
    private String nombre;
    private String apellido;
    private Institucion institucion;
    private Calificacion calificacion;
    private List<Calificacion> calificaciones;
    private String nacionalidad;

}


