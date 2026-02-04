package com.example.demo.cassandra.servicios;

import com.example.demo.mongo.entidad.Calificacion;

public interface ICalificacionServicio {
    Calificacion crearCalificacion(Object o);

    Calificacion registrarCalificacion();
}
