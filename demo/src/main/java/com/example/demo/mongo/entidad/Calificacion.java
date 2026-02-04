package com.example.demo.mongo.entidad;

import org.hibernate.query.sqm.tree.expression.Conversion;

import java.util.List;
import java.util.Map;

public class Calificacion {
    private String id;
    private String estudianteId;
    private String sistemaOrigen; //si es de uk,alemania,etc
    private Object valorOriginal;
    private Map<String, Object> couseworsExams;
    private List<Conversion> conversiones;
    private Auditoria auditoria;
}
