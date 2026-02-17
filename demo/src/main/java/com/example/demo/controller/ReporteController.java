package com.example.demo.controller;
import com.example.demo.model.Reporte;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.service.ReporteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    @Autowired
    private ReporteCassandraRepository cassandraRepo;
    @Autowired
    private ReporteServicio rankingService;

    @GetMapping("/top-paises")
    public List<Reporte> getTopPaises(@RequestParam(defaultValue = "10") int limit) {
        return cassandraRepo.obtenerTopPorTipo("PAIS", limit);
    }

    @GetMapping("/top-institutos")
    public List<Reporte> getTopInstitutos(@RequestParam(defaultValue = "10") int limit) {
        return cassandraRepo.obtenerTopPorTipo("INSTITUTO", limit);

    }
    @PostMapping("/promedios")
    public void guardarPromedios() {
        rankingService.sincronizarDatosAnaliticos();

    }
}
