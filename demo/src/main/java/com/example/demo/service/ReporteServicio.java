package com.example.demo.service;

import com.example.demo.model.Reporte;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteServicio {
    @Autowired
    private ReporteCassandraRepository reporteRepo;
    @Autowired
    private EstudianteNeo4jRepository neo4jRepo; // Aquí agregaremos la query personalizada


    public Map<String, Object> obtenerCuadroDeHonor() {
        Map<String, Object> cuadroHonor = new HashMap<>();

        cuadroHonor.put("mejor_estudiante", reporteRepo.obtenerMejorPorTipo("ESTUDIANTE"));
        cuadroHonor.put("mejor_pais", reporteRepo.obtenerMejorPorTipo("PAIS"));
        cuadroHonor.put("mejor_instituto", reporteRepo.obtenerMejorPorTipo("INSTITUTO"));

        return cuadroHonor;
    }

    public void actualizarRankingsDesdeNeo4j() {

        List<Map<String, Object>> resultados = neo4jRepo.obtenerEstadisticasGlobales();

        reporteRepo.deleteAll();

        for (Map<String, Object> fila : resultados) {
            String nombreEst = (String) fila.get("estudiante");
            String nombreInst = (String) fila.get("institucion");
            String pais = (String) fila.get("pais");
            Double promedio = (Double) fila.get("promedio");

            reporteRepo.save(new Reporte("ESTUDIANTE",promedio, nombreEst ));
            reporteRepo.save(new Reporte("INSTITUTO", promedio,nombreInst));
            reporteRepo.save(new Reporte("PAIS", promedio,pais));
        }
    }
}
