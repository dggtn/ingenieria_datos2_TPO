package com.example.demo.controller;
import com.example.demo.model.Reporte;
import com.example.demo.model.ReporteInstitucionRanking;
import com.example.demo.model.ReporteNivelEducativoRanking;
import com.example.demo.model.ReporteProvinciaRanking;
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

    // Devuelve ranking de paises desde tabla analitica.
    @GetMapping("/top-paises")
    public List<Reporte> getTopPaises(@RequestParam(defaultValue = "10") int limit) {
        return cassandraRepo.obtenerTopPorTipo("PAIS", limit);
    }

    // Devuelve ranking de institutos desde tabla analitica.
    @GetMapping("/top-institutos")
    public List<Reporte> getTopInstitutos(@RequestParam(defaultValue = "10") int limit) {
        return cassandraRepo.obtenerTopPorTipo("INSTITUTO", limit);

    }

    // Construye ranking de instituciones con notas convertidas a Sudafrica.
    @GetMapping("/instituciones-ranking")
    public List<ReporteInstitucionRanking> getInstitucionesRanking() {
        return rankingService.obtenerRankingInstitucionesDesdeCalificaciones();
    }

    // Construye ranking de provincias de Sudafrica.
    @GetMapping("/provincias-ranking")
    public List<ReporteProvinciaRanking> getProvinciasRanking() {
        return rankingService.obtenerRankingProvinciasSudafricaDesdeCalificaciones();
    }

    // Construye ranking de niveles educativos de Sudafrica.
    @GetMapping("/niveles-educativos-ranking")
    public List<ReporteNivelEducativoRanking> getNivelesEducativosRanking() {
        return rankingService.obtenerRankingNivelEducativoSudafricaDesdeCalificaciones();
    }

    // Sincroniza los promedios de Neo4j en Cassandra para analitica.
    @PostMapping("/promedios")
    public void guardarPromedios() {
        rankingService.sincronizarDatosAnaliticos();

    }
}
