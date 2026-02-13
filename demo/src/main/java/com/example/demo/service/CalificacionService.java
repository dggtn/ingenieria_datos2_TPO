package com.example.demo.service;


import com.example.demo.model.Calificacion;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CalificacionService {
    @Autowired
    private CalificacionMONGORepository calificacionRepository;

    public Calificacion registrarCalificacionOriginal(String estId, String matId, String pais, String nota, Map<String, Object> metadatos) {
        Calificacion c = new Calificacion();
        c.setEstudianteId(estId);
        c.setMateriaId(matId);
        c.setPaisOrigen(pais);
        c.setNotaOriginal(nota);
        c.setDetallesOriginales(metadatos);
        c.setAuditor("auditor");
        c.setFechaProcesamiento(LocalDateTime.now());

        String resultadoSA = calcularConversionSudafrica(nota, pais, metadatos);
        c.getConversiones().put("sudafrica", resultadoSA);

        return calificacionRepository.save(c);
    }

    public String calcularConversionSudafrica(String nota, String pais, Map<String, Object> metadatos) {
        if (nota == null || pais == null) return "SIN_DATOS";

        String paisEnMinuscula = pais.toLowerCase();
        double valor = Double.parseDouble(nota);
        double resultado = 0;

        if (paisEnMinuscula.equals("argentina")) {
            resultado = valor * 10;
        }
        else if (paisEnMinuscula.equals("alemania")) {
            resultado = (5.0 - valor) * 25;
        }
        else if (paisEnMinuscula.equals("estados unidos") || paisEnMinuscula.equals("usa")) {
            resultado = (valor / 4.0) * 100;
        }
        else if (paisEnMinuscula.equals("inglaterra") || paisEnMinuscula.equals("uk")) {
            resultado = (valor / 9.0) * 100;
        }
        else {
            return "No se permite conversión";
        }

        return resultado + "%";
    }
}






