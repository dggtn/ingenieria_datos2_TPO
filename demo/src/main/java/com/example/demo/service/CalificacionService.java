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
        c.setAuditor("SISTEMA_CARGA_OFICIAL");
        c.setFechaProcesamiento(LocalDateTime.now());

        String resultadoSA = calcularConversionSudafrica(nota, pais, metadatos);
        c.getConversiones().put("sudafrica", resultadoSA);

        return calificacionRepository.save(c);
    }

    private String calcularConversionSudafrica(String nota, String pais, Map<String, Object> metadatos) {
        if (nota == null || pais == null) return "SIN_DATOS";

        return switch (pais.toUpperCase()) {
            case "ARGENTINA" -> {
                double promedio = metadatos.containsKey("promedio")
                        ? Double.parseDouble(metadatos.get("promedio").toString())
                        : Double.parseDouble(nota);

                yield (promedio >= 7.0) ? "75% (Merit)" : "50% (Pass)";
            }

            case "USA" -> {
                double gpa = metadatos.containsKey("gpa")
                        ? Double.parseDouble(metadatos.get("gpa").toString())
                        : 0.0;

                if (gpa >= 3.5 || nota.equalsIgnoreCase("A")) yield "80% (Distinction)";
                yield "60% (Pass)";
            }

            case "ALEMANIA" -> {
                double notaGer = Double.parseDouble(nota);
                if (notaGer <= 1.5) yield "90% (Outstanding)";
                if (notaGer <= 3.0) yield "70% (Satisfactory)";
                yield "40% (Fail)";
            }

            case "UK" -> {
                yield switch (nota.toUpperCase()) {
                    case "A*", "A" -> "85% (Distinction)";
                    case "B", "C" -> "65% (Merit)";
                    default -> "REVISIÓN MANUAL REQUERIDA";
                };
            }

            default -> "PAIS_NO_SOPORTADO";
        };
    }
}






