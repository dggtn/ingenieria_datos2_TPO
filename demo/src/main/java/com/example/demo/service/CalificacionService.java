package com.example.demo.service;

import com.example.demo.model.Calificacion;
import com.example.demo.model.LegislacionConversion;
import com.example.demo.model.Reporte;
import com.example.demo.model.ReportePromedio;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.LegislacionConversionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionMONGORepository calificacionRepository;
    @Autowired
    private EstudianteNeo4jRepository neo4jRepository;
    @Autowired
    private ReporteCassandraRepository cassandraRepository;
    @Autowired
    private LegislacionConversionMONGORepository legislacionRepository;

    private Double aDouble(Object valor) {
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        if (valor instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return 0.0;
    }

    public void actualizarRankingsNacionales() {
        List<ReportePromedio> promediosPais = neo4jRepository.calcularpromedioPorPais();
        List<ReportePromedio> promediosInst = neo4jRepository.calcularPromedioPorInstitucion();

        List<Reporte> listaParaCassandra = new ArrayList<>();

        promediosPais.forEach(m -> listaParaCassandra.add(new Reporte("PAIS", m.getPromedio(), m.getNombre())));
        promediosInst.forEach(m -> listaParaCassandra.add(new Reporte("INSTITUTO", m.getPromedio(), m.getNombre())));

        cassandraRepository.saveAll(listaParaCassandra);
    }

    public Calificacion registrarCalificacionOriginal(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion c = new Calificacion();
        c.setEstudianteId(requestRegistrarCalificacion.getEstudiante());
        c.setMateriaId(requestRegistrarCalificacion.getMateria());
        c.setPaisOrigen(requestRegistrarCalificacion.getPaisOrigen());
        c.setMetadata(requestRegistrarCalificacion.getMetadatos());
        c.setAuditor("auditor");
        c.setFechaProcesamiento(LocalDateTime.now());

        Double resultadoSA = calcularConversionSudafrica(requestRegistrarCalificacion);
        c.setConversiones(resultadoSA);

        String idEstudiante = c.getEstudianteId();
        String idInstitucion = requestRegistrarCalificacion.getInstitucion();
        String idMateria = requestRegistrarCalificacion.getMateria();
        String periodo = (String) requestRegistrarCalificacion.getMetadatos().get("periodo");
        String nivel = (String) requestRegistrarCalificacion.getMetadatos().get("nivel");

        neo4jRepository.registrarDondeEstudio(idEstudiante, idInstitucion, periodo);
        neo4jRepository.registrarCursada(idEstudiante, idMateria, resultadoSA, periodo, nivel);
        neo4jRepository.registrarDondeDictaMateria(idInstitucion, idMateria, periodo, nivel);
        return calificacionRepository.save(c);
    }

    public Double calcularConversionSudafrica(RequestRegistrarCalificacion request) {
        if (request == null || request.getPaisOrigen() == null) {
            return 0.0;
        }

        LegislacionConversion regla = seleccionarRegla(resolverFechaNormativa(request));
        String pais = normalizarPais(request.getPaisOrigen());

        return switch (pais) {
            case "argentina" -> convertirArgentina(request.getMetadatos(), regla);
            case "usa" -> convertirUsa(request.getMetadatos(), regla);
            case "uk" -> convertirUk(request.getMetadatos(), regla);
            case "alemania" -> convertirAlemania(request.getMetadatos(), regla);
            default -> 0.0;
        };
    }

    private LegislacionConversion seleccionarRegla(LocalDate fecha) {
        List<LegislacionConversion> reglas = legislacionRepository.findAll();
        if (reglas.isEmpty()) {
            throw new IllegalStateException("No existe legislacion de conversion en MongoDB");
        }

        return reglas.stream()
                .filter(v -> !fecha.isBefore(v.getVigenciaDesde())
                        && (v.getVigenciaHasta() == null || !fecha.isAfter(v.getVigenciaHasta())))
                .max(Comparator.comparingInt(LegislacionConversion::getVersion))
                .orElseGet(() -> reglas.stream()
                        .max(Comparator.comparingInt(LegislacionConversion::getVersion))
                        .orElseThrow(() -> new IllegalStateException("No hay legislacion disponible")));
    }

    private LocalDate resolverFechaNormativa(RequestRegistrarCalificacion request) {
        Map<String, Object> metadatos = request.getMetadatos();
        if (metadatos == null) {
            return LocalDate.now();
        }

        Object fechaNormativa = metadatos.get("fecha_normativa");
        if (fechaNormativa instanceof String s && !s.isBlank()) {
            try {
                return LocalDate.parse(s);
            } catch (Exception ignored) {
            }
        }

        Object anio = metadatos.get("anio_normativa");
        if (anio instanceof Number n) {
            return LocalDate.of(n.intValue(), 1, 1);
        }

        return LocalDate.now();
    }

    private String normalizarPais(String paisOrigen) {
        String p = paisOrigen.toLowerCase().trim();
        if (p.equals("estados unidos")) {
            return "usa";
        }
        if (p.equals("inglaterra") || p.equals("reino unido")) {
            return "uk";
        }
        return p;
    }

    private double convertirArgentina(Map<String, Object> metadatos, LegislacionConversion regla) {
        double primer = convertirNotaArgentina((int) Math.round(aDouble(metadatos.get("primer_parcial"))), regla);
        double segundo = convertirNotaArgentina((int) Math.round(aDouble(metadatos.get("segundo_parcial"))), regla);
        double examen = convertirNotaArgentina((int) Math.round(aDouble(metadatos.get("examen_final"))), regla);
        return (primer + segundo + examen) / 3.0;
    }

    private double convertirNotaArgentina(int nota, LegislacionConversion regla) {
        if (nota < 4) {
            return 30.0;
        }
        return regla.getArgentinaNotas().getOrDefault(nota, 30.0);
    }

    private double convertirUk(Map<String, Object> metadatos, LegislacionConversion regla) {
        double coursework = convertirNotaUk((String) metadatos.get("coursework"), regla);
        double mock = convertirNotaUk((String) metadatos.get("mock_exam"), regla);
        double finalGrade = convertirNotaUk((String) metadatos.get("final_grade"), regla);
        return (coursework + mock + finalGrade) / 3.0;
    }

    private double convertirNotaUk(String nota, LegislacionConversion regla) {
        if (nota == null) {
            return 40.0;
        }
        return regla.getUkNotas().getOrDefault(nota.trim().toUpperCase(), 40.0);
    }

    private double convertirUsa(Map<String, Object> metadatos, LegislacionConversion regla) {
        String semester1 = metadatos.get("semester") == null ? "F" : metadatos.get("semester").toString().trim().toUpperCase();
        String semester2 = metadatos.get("semester_2") == null ? "F" : metadatos.get("semester_2").toString().trim().toUpperCase();

        double semester1Eq = regla.getUsaSemester().getOrDefault(semester1, 30.0);
        double semester2Eq = regla.getUsaSemester().getOrDefault(semester2, 30.0);

        return (semester1Eq + semester2Eq) / 2.0;
    }

    private double convertirAlemania(Map<String, Object> metadatos, LegislacionConversion regla) {
        double klassenArbeit = aDouble(metadatos.get("KlassenArbeit"));
        double mundlichArbeit = aDouble(metadatos.get("MundlichArbeit"));
        return (regla.getAlemaniaBase() - ((klassenArbeit + mundlichArbeit) / 2.0)) * regla.getAlemaniaFactor();
    }
}
