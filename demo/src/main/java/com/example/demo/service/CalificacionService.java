package com.example.demo.service;

import com.example.demo.model.Calificacion;
import com.example.demo.model.CalificacionCassandra;
import com.example.demo.model.Estudiante;
import com.example.demo.model.Institucion;
import com.example.demo.model.LegislacionConversion;
import com.example.demo.model.Reporte;
import com.example.demo.model.ReportePromedio;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.repository.cassandra.CalificacionCassandraRepository;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.LegislacionConversionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CalificacionService {
    private static final DateTimeFormatter PERIOD_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    private CalificacionMONGORepository calificacionRepository;
    @Autowired
    private EstudianteNeo4jRepository neo4jRepository;
    @Autowired
    private InstitucionNeo4jRepository institucionNeo4jRepository;
    @Autowired
    private ReporteCassandraRepository cassandraRepository;
    @Autowired
    private CalificacionCassandraRepository calificacionCassandraRepository;
    @Autowired
    private LegislacionConversionMONGORepository legislacionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Convierte cualquier valor numerico/string a Double de forma segura.
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

    // Recalcula rankings en Neo4j y sincroniza resultados en Cassandra.
    public void actualizarRankingsNacionales() {
        List<ReportePromedio> promediosPais = neo4jRepository.calcularpromedioPorPais();
        List<ReportePromedio> promediosInst = neo4jRepository.calcularPromedioPorInstitucion();

        List<Reporte> listaParaCassandra = new ArrayList<>();

        promediosPais.forEach(m -> listaParaCassandra.add(new Reporte("PAIS", m.getPromedio(), m.getNombre())));
        promediosInst.forEach(m -> listaParaCassandra.add(new Reporte("INSTITUTO", m.getPromedio(), m.getNombre())));

        cassandraRepository.saveAll(listaParaCassandra);
    }

    // Registra una calificacion nueva y actualiza Neo4j, Mongo y auditoria Cassandra.
    public Calificacion registrarCalificacionOriginal(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion c = new Calificacion();
        c.setEstudianteId(requestRegistrarCalificacion.getEstudiante());
        c.setMateriaId(requestRegistrarCalificacion.getMateria());
        c.setPaisOrigen(requestRegistrarCalificacion.getPaisOrigen());
        c.setMetadata(requestRegistrarCalificacion.getMetadatos());
        c.setAuditor("auditor");
        c.setFechaProcesamiento(LocalDateTime.now());
        boolean institucionSudafrica = esInstitucionSudafrica(requestRegistrarCalificacion);
        if (institucionSudafrica) {
            String scoreIngresado = extraerScoreIngresado(requestRegistrarCalificacion.getMetadatos());
            c.setNotaOriginal(scoreIngresado);
            c.setNotaOriginalNumerica(aDouble(scoreIngresado));
        } else {
            c.setNotaOriginal(construirNotaOriginalTexto(requestRegistrarCalificacion));
            c.setNotaOriginalNumerica(resultadoPromedioOriginalNumerico(requestRegistrarCalificacion));
        }

        Double resultadoSA = institucionSudafrica
                ? c.getNotaOriginalNumerica()
                : calcularConversionSudafrica(requestRegistrarCalificacion);
        c.setConversiones(resultadoSA);

        String idEstudiante = c.getEstudianteId();
        String idInstitucion = requestRegistrarCalificacion.getInstitucion();
        String idMateria = requestRegistrarCalificacion.getMateria();
        String periodo = LocalDate.now().format(PERIOD_FORMAT);
        String nivel = nullableMeta(requestRegistrarCalificacion.getMetadatos(), "nivel");
        String notaOriginalTexto = c.getNotaOriginal();
        Double promedioOriginal = c.getNotaOriginalNumerica();
        if (promedioOriginal == null) {
            promedioOriginal = resultadoSA;
        }

        neo4jRepository.registrarDondeEstudio(idEstudiante, idInstitucion, periodo);
        neo4jRepository.registrarCursada(idEstudiante, idMateria, promedioOriginal, periodo, nivel, notaOriginalTexto);
        Calificacion guardada = calificacionRepository.save(c);
        registrarAuditoriaCassandra("CREACION", guardada, requestRegistrarCalificacion);
        return guardada;
    }

    // Modifica una calificacion existente y refresca relaciones/bitacora asociadas.
    public Calificacion modificarCalificacion(String calificacionId, RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion existente = calificacionRepository.findById(calificacionId)
                .orElseThrow(() -> new IllegalArgumentException("No existe calificacion con id " + calificacionId));

        existente.setEstudianteId(requestRegistrarCalificacion.getEstudiante());
        existente.setMateriaId(requestRegistrarCalificacion.getMateria());
        existente.setPaisOrigen(requestRegistrarCalificacion.getPaisOrigen());
        existente.setMetadata(requestRegistrarCalificacion.getMetadatos());
        existente.setFechaProcesamiento(LocalDateTime.now());
        boolean institucionSudafrica = esInstitucionSudafrica(requestRegistrarCalificacion);
        if (institucionSudafrica) {
            String scoreIngresado = extraerScoreIngresado(requestRegistrarCalificacion.getMetadatos());
            existente.setNotaOriginal(scoreIngresado);
            existente.setNotaOriginalNumerica(aDouble(scoreIngresado));
            existente.setConversiones(existente.getNotaOriginalNumerica());
        } else {
            existente.setNotaOriginal(construirNotaOriginalTexto(requestRegistrarCalificacion));
            existente.setNotaOriginalNumerica(resultadoPromedioOriginalNumerico(requestRegistrarCalificacion));
            existente.setConversiones(calcularConversionSudafrica(requestRegistrarCalificacion));
        }

        String idEstudiante = existente.getEstudianteId();
        String idInstitucion = requestRegistrarCalificacion.getInstitucion();
        String idMateria = requestRegistrarCalificacion.getMateria();
        String periodo = LocalDate.now().format(PERIOD_FORMAT);
        String nivel = nullableMeta(requestRegistrarCalificacion.getMetadatos(), "nivel");
        String notaOriginalTexto = existente.getNotaOriginal();
        Double promedioOriginal = existente.getNotaOriginalNumerica();
        if (promedioOriginal == null) {
            promedioOriginal = existente.getConversiones();
        }

        neo4jRepository.registrarDondeEstudio(idEstudiante, idInstitucion, periodo);
        neo4jRepository.registrarCursada(idEstudiante, idMateria, promedioOriginal, periodo, nivel, notaOriginalTexto);

        Calificacion actualizada = calificacionRepository.save(existente);
        registrarAuditoriaCassandra("MODIFICACION", actualizada, requestRegistrarCalificacion);
        return actualizada;
    }

    // Convierte la nota de origen a la escala Sudafrica segun legislacion vigente.
    public Double calcularConversionSudafrica(RequestRegistrarCalificacion request) {
        if (request == null || request.getPaisOrigen() == null) {
            return 0.0;
        }
        if (esInstitucionSudafrica(request)) {
            return aDouble(extraerScoreIngresado(request.getMetadatos()));
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

    // Selecciona la regla de conversion valida para una fecha dada.
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

    // Resuelve la fecha normativa desde metadatos o usa fecha actual.
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

    // Normaliza variantes de texto de pais a claves internas estables.
    private String normalizarPais(String paisOrigen) {
        if (paisOrigen == null) {
            return "";
        }
        String p = paisOrigen.toLowerCase().trim();
        if (p.equals("estados unidos")) {
            return "usa";
        }
        if (p.equals("inglaterra") || p.equals("reino unido")) {
            return "uk";
        }
        if (p.equals("sudafrica") || p.equals("sudáfrica") || p.equals("south africa")) {
            return "sudafrica";
        }
        return p;
    }

    // Verifica si la institucion de la solicitud pertenece a Sudafrica.
    private boolean esInstitucionSudafrica(RequestRegistrarCalificacion request) {
        if (request == null || request.getInstitucion() == null || request.getInstitucion().isBlank()) {
            return false;
        }
        Institucion institucion = institucionNeo4jRepository.findById(request.getInstitucion()).orElse(null);
        if (institucion == null || institucion.getPais() == null) {
            return false;
        }
        return "sudafrica".equals(normalizarPais(institucion.getPais()));
    }

    // Extrae la nota ingresada para Sudafrica desde distintos nombres de campo.
    private String extraerScoreIngresado(Map<String, Object> metadatos) {
        if (metadatos == null) {
            return "0";
        }
        Object score = metadatos.get("score");
        if (score == null) {
            score = metadatos.get("nota");
        }
        if (score == null) {
            score = metadatos.get("resultado");
        }
        if (score == null) {
            score = metadatos.get("valor");
        }
        return score == null ? "0" : String.valueOf(score).trim();
    }

    // Convierte promedio de Argentina a escala Sudafrica.
    private double convertirArgentina(Map<String, Object> metadatos, LegislacionConversion regla) {
        double primer = convertirNotaArgentina((int) Math.round(aDouble(metadatos.get("primer_parcial"))), regla);
        double segundo = convertirNotaArgentina((int) Math.round(aDouble(metadatos.get("segundo_parcial"))), regla);
        double examen = convertirNotaArgentina((int) Math.round(aDouble(metadatos.get("examen_final"))), regla);
        return (primer + segundo + examen) / 3.0;
    }

    // Convierte una nota puntual argentina usando la tabla de legislacion.
    private double convertirNotaArgentina(int nota, LegislacionConversion regla) {
        if (nota < 4) {
            return 30.0;
        }
        return regla.getArgentinaNotas().getOrDefault(nota, 30.0);
    }

    // Convierte promedio UK (coursework/mock/final) a escala Sudafrica.
    private double convertirUk(Map<String, Object> metadatos, LegislacionConversion regla) {
        double coursework = convertirNotaUk((String) metadatos.get("coursework"), regla);
        double mock = convertirNotaUk((String) metadatos.get("mock_exam"), regla);
        double finalGrade = convertirNotaUk((String) metadatos.get("final_grade"), regla);
        return (coursework + mock + finalGrade) / 3.0;
    }

    // Convierte una calificacion literal UK a su equivalente Sudafrica.
    private double convertirNotaUk(String nota, LegislacionConversion regla) {
        if (nota == null) {
            return 40.0;
        }
        return regla.getUkNotas().getOrDefault(nota.trim().toUpperCase(), 40.0);
    }

    // Convierte promedio USA (semester 1 y 2) a escala Sudafrica.
    private double convertirUsa(Map<String, Object> metadatos, LegislacionConversion regla) {
        String semester1 = metadatos.get("semester") == null ? "F" : metadatos.get("semester").toString().trim().toUpperCase();
        String semester2 = metadatos.get("semester_2") == null ? "F" : metadatos.get("semester_2").toString().trim().toUpperCase();

        double semester1Eq = regla.getUsaSemester().getOrDefault(semester1, 30.0);
        double semester2Eq = regla.getUsaSemester().getOrDefault(semester2, 30.0);

        return (semester1Eq + semester2Eq) / 2.0;
    }

    // Convierte promedio Alemania a escala Sudafrica con formula normativa.
    private double convertirAlemania(Map<String, Object> metadatos, LegislacionConversion regla) {
        double klassenArbeit = aDouble(metadatos.get("KlassenArbeit"));
        double mundlichArbeit = aDouble(metadatos.get("MundlichArbeit"));
        return (regla.getAlemaniaBase() - ((klassenArbeit + mundlichArbeit) / 2.0)) * regla.getAlemaniaFactor();
    }

    // Guarda un snapshot auditable de la calificacion en Cassandra.
    private void registrarAuditoriaCassandra(String operacion, Calificacion calificacion, RequestRegistrarCalificacion request) {
        Estudiante estudiante = neo4jRepository.findById(calificacion.getEstudianteId()).orElse(null);
        Institucion institucion = institucionNeo4jRepository.findById(request.getInstitucion()).orElse(null);

        CalificacionCassandra fila = new CalificacionCassandra();
        fila.setEventoId(UUID.randomUUID().toString());
        fila.setOperacion(operacion);
        fila.setFechaEvento(LocalDateTime.now());

        fila.setCalificacionId(calificacion.getId());

        fila.setEstudianteId(estudiante != null ? estudiante.getId() : calificacion.getEstudianteId());
        fila.setEstudianteNombre(estudiante != null ? estudiante.getNombre() : null);
        fila.setEstudianteEmail(estudiante != null ? estudiante.getEmail() : null);
        fila.setEstudiantePaisOrigen(estudiante != null ? estudiante.getPaisOrigen() : null);
        fila.setEstudianteInstitucionActual(estudiante != null ? estudiante.getInstitucionActual() : null);
        fila.setEstudianteHistorialJson(toJson(estudiante != null ? estudiante.getHistorial() : null));
        fila.setEstudianteHistorialAcademicoJson(toJson(estudiante != null ? estudiante.getHistorialAcademico() : null));
        fila.setEstudianteMateriasJson(toJson(estudiante != null ? estudiante.getMaterias() : null));

        fila.setInstitucionId(institucion != null ? institucion.getId() : request.getInstitucion());
        fila.setInstitucionNombre(institucion != null ? institucion.getNombre() : null);
        fila.setInstitucionPais(institucion != null ? institucion.getPais() : null);
        fila.setInstitucionProvincia(institucion != null ? institucion.getProvincia() : null);
        fila.setInstitucionNivelEducativo(institucion != null ? institucion.getNivelEducativo() : null);
        fila.setInstitucionCurriculumJson(toJson(institucion != null ? institucion.getCurriculum() : null));

        fila.setNotaPaisOrigen(calificacion.getPaisOrigen());
        fila.setNotaMateriaId(calificacion.getMateriaId());
        fila.setNotaOriginal(calificacion.getNotaOriginal());
        fila.setNotaOriginalNumerica(calificacion.getNotaOriginalNumerica());
        fila.setNotaConversionSudafrica(calificacion.getConversiones());
        fila.setNotaAuditor(calificacion.getAuditor());
        fila.setNotaFechaProcesamiento(calificacion.getFechaProcesamiento());
        fila.setNotaMetadataJson(toJson(calificacion.getMetadata()));
        fila.setNotaSnapshotJson(toJson(calificacion));

        calificacionCassandraRepository.save(fila);
    }

    // Devuelve metadato string limpio o null cuando no esta informado.
    private String nullableMeta(Map<String, Object> metadatos, String key) {
        if (metadatos == null || metadatos.get(key) == null) {
            return null;
        }
        String value = String.valueOf(metadatos.get(key)).trim();
        return value.isEmpty() ? null : value;
    }

    // Calcula promedio original numerico segun la escala del pais de origen.
    private double resultadoPromedioOriginalNumerico(RequestRegistrarCalificacion request) {
        Map<String, Object> metadatos = request.getMetadatos();
        if (metadatos == null) {
            return 0.0;
        }

        String pais = normalizarPais(request.getPaisOrigen());
        if ("argentina".equals(pais)) {
            return (aDouble(metadatos.get("primer_parcial"))
                    + aDouble(metadatos.get("segundo_parcial"))
                    + aDouble(metadatos.get("examen_final"))) / 3.0;
        }
        if ("usa".equals(pais)) {
            double promedio = (convertirEscalaUsaRaw(metadatos.get("semester"))
                    + convertirEscalaUsaRaw(metadatos.get("semester_2"))) / 2.0;
            return normalizarEscalaUsa(promedio);
        }
        if ("uk".equals(pais)) {
            double promedio = (convertirEscalaUkRaw(metadatos.get("coursework"))
                    + convertirEscalaUkRaw(metadatos.get("mock_exam"))
                    + convertirEscalaUkRaw(metadatos.get("final_grade"))) / 3.0;
            return normalizarEscalaUk(promedio);
        }
        if ("alemania".equals(pais)) {
            double promedio = (aDouble(metadatos.get("KlassenArbeit")) + aDouble(metadatos.get("MundlichArbeit"))) / 2.0;
            return normalizarEscalaAlemania(promedio);
        }
        return 0.0;
    }

    // Construye la representacion textual de la nota original por pais.
    private String construirNotaOriginalTexto(RequestRegistrarCalificacion request) {
        Map<String, Object> metadatos = request.getMetadatos();
        if (metadatos == null) {
            return "";
        }
        String pais = normalizarPais(request.getPaisOrigen());
        if ("argentina".equals(pais)) {
            return formatearDecimal((aDouble(metadatos.get("primer_parcial"))
                    + aDouble(metadatos.get("segundo_parcial"))
                    + aDouble(metadatos.get("examen_final"))) / 3.0);
        }
        if ("usa".equals(pais)) {
            double promedio = (convertirEscalaUsaRaw(metadatos.get("semester"))
                    + convertirEscalaUsaRaw(metadatos.get("semester_2"))) / 2.0;
            return escalaUsaTexto(normalizarEscalaUsa(promedio));
        }
        if ("uk".equals(pais)) {
            double promedio = (convertirEscalaUkRaw(metadatos.get("coursework"))
                    + convertirEscalaUkRaw(metadatos.get("mock_exam"))
                    + convertirEscalaUkRaw(metadatos.get("final_grade"))) / 3.0;
            return escalaUkTexto(normalizarEscalaUk(promedio));
        }
        if ("alemania".equals(pais)) {
            double promedio = (aDouble(metadatos.get("KlassenArbeit")) + aDouble(metadatos.get("MundlichArbeit"))) / 2.0;
            return formatearDecimal(normalizarEscalaAlemania(promedio));
        }
        return "";
    }

    // Mapea calificaciones USA en letras a valor numerico interno.
    private double convertirEscalaUsaRaw(Object valor) {
        if (valor == null) {
            return 0.0;
        }
        return switch (valor.toString().trim().toUpperCase()) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }

    // Mapea calificaciones UK en letras a valor numerico interno.
    private double convertirEscalaUkRaw(Object valor) {
        if (valor == null) {
            return 0.0;
        }
        return switch (valor.toString().trim().toUpperCase()) {
            case "A*" -> 7.0;
            case "A" -> 6.0;
            case "B" -> 5.0;
            case "C" -> 4.0;
            case "D" -> 3.0;
            case "E" -> 2.0;
            case "F" -> 1.0;
            default -> 0.0;
        };
    }

    // Ajusta un promedio al valor valido mas cercano de la escala USA.
    private double normalizarEscalaUsa(double promedio) {
        double[] escala = {4.0, 3.0, 2.0, 1.0, 0.0};
        double elegido = escala[0];
        double mejorDist = Math.abs(promedio - elegido);
        for (double v : escala) {
            double d = Math.abs(promedio - v);
            if (d < mejorDist) {
                mejorDist = d;
                elegido = v;
            }
        }
        return elegido;
    }

    // Ajusta un promedio al rango permitido de la escala UK.
    private double normalizarEscalaUk(double promedio) {
        long redondeado = Math.round(promedio);
        if (redondeado < 1) {
            return 1.0;
        }
        if (redondeado > 7) {
            return 7.0;
        }
        return (double) redondeado;
    }

    // Ajusta un promedio al rango 1.0-6.0 de la escala alemana.
    private double normalizarEscalaAlemania(double promedio) {
        double acotado = Math.max(1.0, Math.min(6.0, promedio));
        return Math.round(acotado * 10.0) / 10.0;
    }

    // Convierte valor numerico interno a letra equivalente USA.
    private String escalaUsaTexto(double valor) {
        if (valor >= 4.0) {
            return "A";
        }
        if (valor >= 3.0) {
            return "B";
        }
        if (valor >= 2.0) {
            return "C";
        }
        if (valor >= 1.0) {
            return "D";
        }
        return "F";
    }

    // Convierte valor numerico interno a letra equivalente UK.
    private String escalaUkTexto(double valor) {
        int v = (int) Math.round(valor);
        return switch (v) {
            case 7 -> "A*";
            case 6 -> "A";
            case 5 -> "B";
            case 4 -> "C";
            case 3 -> "D";
            case 2 -> "E";
            default -> "F";
        };
    }

    // Formatea un decimal con un digito fraccional.
    private String formatearDecimal(double valor) {
        return String.format(java.util.Locale.US, "%.1f", valor);
    }

    // Serializa un objeto a JSON para auditoria o snapshots.
    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"serialization_failed\"}";
        }
    }
}
