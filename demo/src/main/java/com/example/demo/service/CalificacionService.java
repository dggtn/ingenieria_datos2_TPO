package com.example.demo.service;


import com.example.demo.model.Calificacion;
import com.example.demo.repository.mongo.CalificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CalificacionService {
    @Autowired
    private CalificacionRepository calificacionRepository;

    public Calificacion registrarCalificacionOriginal(String estId, String matId, String pais, String nota, Map<String, Object> metadatos) {
        Calificacion c = new Calificacion(); // Ahora sí puedes instanciarla
        c.setEstudianteId(estId); // Ahora el método existe
        c.setMateriaId(matId);
        c.setPaisOrigen(pais);
        c.setNotaOriginal(nota);
        c.setDetallesOriginales(metadatos);
        // RF5: Auditoría y Trazabilidad (Inmutable)
        c.setAuditor("SISTEMA_CARGA_OFICIAL");
        c.setFechaProcesamiento(LocalDateTime.now());

        // RF2: Disparamos la conversión automática a Sudáfrica
        String resultadoSA = calcularConversionSudafrica(nota, pais, metadatos);
        c.getConversiones().put("sudafrica", resultadoSA);

        return calificacionRepository.save(c);
    }

    private String calcularConversionSudafrica(String nota, String pais, Map<String, Object> metadatos) {
        if (nota == null || pais == null) return "SIN_DATOS";

        return switch (pais.toUpperCase()) {
            case "ARGENTINA" -> {
                // En Argentina, si no mandan el promedio en los metadatos, usamos la notaBase
                double promedio = metadatos.containsKey("promedio")
                        ? Double.parseDouble(metadatos.get("promedio").toString())
                        : Double.parseDouble(nota);

                yield (promedio >= 7.0) ? "75% (Merit)" : "50% (Pass)";
            }

            case "USA" -> {
                // En USA, priorizamos el GPA si viene en la "ensalada"
                double gpa = metadatos.containsKey("gpa")
                        ? Double.parseDouble(metadatos.get("gpa").toString())
                        : 0.0;

                if (gpa >= 3.5 || nota.equalsIgnoreCase("A")) yield "80% (Distinction)";
                yield "60% (Pass)";
            }

            case "ALEMANIA" -> {
                // Escala inversa: 1 es lo mejor, 4 es aprobado
                double notaGer = Double.parseDouble(nota);
                if (notaGer <= 1.5) yield "90% (Outstanding)";
                if (notaGer <= 3.0) yield "70% (Satisfactory)";
                yield "40% (Fail)";
            }

            case "UK" -> {
                // Sistema GCSE/A-Levels: Generalmente basado en letras
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






