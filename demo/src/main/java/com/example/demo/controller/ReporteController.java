package com.example.demo.controller;
import com.example.demo.model.Reporte;
import com.example.demo.service.ReporteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteServicio reporte;

    @PostMapping("/reportes/procesar")
    public ResponseEntity<String> procesar() {
        reporte.actualizarRankingsDesdeNeo4j();
        return ResponseEntity.ok("Reportes actualizados en Cassandra.");
    }
    @GetMapping("/top")
    public ResponseEntity<Map<String, Object>> obtenerTop() {

        Map<String, Object>cuadroDeHonor = reporte.obtenerCuadroDeHonor();

        return ResponseEntity.ok(cuadroDeHonor);
    }

}
