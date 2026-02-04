package com.example.demo.cassandra.dto;

import com.example.demo.cassandra.entidad.Calificacion;
import com.example.demo.cassandra.entidad.Institucion;
import lombok.*;

import java.util.List;


@Data

public class EstudianteDTO {
    private String nombre;
    private String apellido;
    private Institucion institucion;
    private Calificacion calificacion;
    private List<Calificacion> trayectoria;
    private String nacionalidad;


}


