package com.example.demo.controller;
import com.example.demo.model.Reporte;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteCassandraRepository reporteRepository;

    @GetMapping("/ranking-alerta")
    public ResponseEntity<String> obtenerRankingParaAlert() {
        List<Reporte> top = reporteRepository.obtenerTop5Global();

        StringBuilder sb = new StringBuilder(" TOP 5 ESTUDIANTES :\n\n");
        for (int i = 0; i < top.size(); i++) {
            Reporte r = top.get(i);
            sb.append((i + 1) + ". " + r.getEstudianteid() + " - Promedio: " + r.getPromedio() + "%\n");
        }

        return ResponseEntity.ok(sb.toString());
    }
}
