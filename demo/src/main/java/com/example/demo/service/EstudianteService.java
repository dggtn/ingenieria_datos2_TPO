package com.example.demo.service;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Institucion;
import com.example.demo.model.Materia;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteMONGORepository estudianteRepository;

    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private InstitucionMONGORepository institucionRepository;

    @Autowired
    private EquivalenciaCalificacionService equivalenciaCalificacionService;

    public Estudiante registrarEstudiante(Estudiante estudiante) {
        if (estudiante.getId() == null || estudiante.getId().isBlank()) {
            estudiante.setId(UUID.randomUUID().toString());
        }

        Estudiante guardado = estudianteRepository.save(estudiante);
        sincronizarEstudianteEnNeo4j(guardado);
        return guardado;
    }

    public List<Estudiante> registrarEstudiantes(List<Estudiante> estudiantes) {
        List<Estudiante> guardados = new ArrayList<>();
        if (estudiantes == null) {
            return guardados;
        }
        for (Estudiante estudiante : estudiantes) {
            guardados.add(registrarEstudiante(estudiante));
        }
        return guardados;
    }

    public Estudiante actualizarEstudiante(String id, Estudiante estudiante) {
        Optional<Estudiante> existente = estudianteRepository.findById(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No existe estudiante con id: " + id);
        }
        estudiante.setId(id);
        Estudiante actualizado = estudianteRepository.save(estudiante);
        sincronizarEstudianteEnNeo4j(actualizado);
        return actualizado;
    }

    public Estudiante obtenerPorId(String id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe estudiante con id: " + id));
    }

    public void eliminarEstudiante(String id) {
        if (!estudianteRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe estudiante con id: " + id);
        }
        estudianteRepository.deleteById(id);
        neo4jClient.query("""
                MATCH (e:Estudiante {id: $id})
                DETACH DELETE e
                """)
                .bind(id).to("id")
                .run();
    }

    public void eliminarEstudiantes(List<String> ids) {
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            if (id != null && !id.isBlank()) {
                eliminarEstudiante(id);
            }
        }
    }

    private void sincronizarEstudianteEnNeo4j(Estudiante guardado) {
        String fechaNacimiento = guardado.getFechaNacimiento() != null ? guardado.getFechaNacimiento().toString() : "";
        List<MateriaCursoContexto> materiasObjetivo = new ArrayList<>();

        neo4jClient.query("""
                MERGE (e:Estudiante {id: $id})
                SET e.nombre = $nombre,
                    e.name = $nombre,
                    e.idNacional = $idNacional,
                    e.fechaNacimiento = $fechaNacimiento,
                    e.provincia = $provincia
                """)
                .bind(guardado.getId()).to("id")
                .bind(guardado.getNombre()).to("nombre")
                .bind(guardado.getIdNacional()).to("idNacional")
                .bind(fechaNacimiento).to("fechaNacimiento")
                .bind(guardado.getProvincia()).to("provincia")
                .run();

        if (guardado.getInstitucionActualId() != null && !guardado.getInstitucionActualId().isBlank()) {
            asociarInstitucionPrevia(guardado.getId(), guardado.getInstitucionActualId(), true);
        }

        if (guardado.getInstitucionActualId() != null
                && !guardado.getInstitucionActualId().isBlank()
                && guardado.getMateriasInstitucionActual() != null) {
            for (Materia materiaActual : guardado.getMateriasInstitucionActual()) {
                materiasObjetivo.add(new MateriaCursoContexto(guardado.getInstitucionActualId(), materiaActual));
            }
        }

        if (guardado.getHistorialAcademico() != null) {
            for (Estudiante.HistorialInstitucion historialInstitucion : guardado.getHistorialAcademico()) {
                if (historialInstitucion.getInstitucionId() == null || historialInstitucion.getInstitucionId().isBlank()) {
                    continue;
                }

                neo4jClient.query("""
                        MERGE (i:Institucion {id: $institucionId})
                        SET i.nombre = coalesce($institucionNombre, i.nombre, i.id),
                            i.name = coalesce($institucionNombre, i.name, i.nombre, i.id)
                        """)
                        .bind(historialInstitucion.getInstitucionId()).to("institucionId")
                        .bind(historialInstitucion.getInstitucionNombre()).to("institucionNombre")
                        .run();

                asociarInstitucionPrevia(guardado.getId(), historialInstitucion.getInstitucionId(), false);

                if (historialInstitucion.getMaterias() == null) {
                    continue;
                }

                for (Materia historialMateria : historialInstitucion.getMaterias()) {
                    materiasObjetivo.add(new MateriaCursoContexto(historialInstitucion.getInstitucionId(), historialMateria));
                }
            }
        }

        sincronizarRelacionesCurso(guardado.getId(), materiasObjetivo);
    }

    private void sincronizarRelacionesCurso(String estudianteId, List<MateriaCursoContexto> materiasObjetivo) {
        Set<String> materiasVigentes = new HashSet<>();
        for (MateriaCursoContexto contexto : materiasObjetivo) {
            if (contexto == null || contexto.materia() == null) {
                continue;
            }
            String materiaId = contexto.materia().getMateriaId();
            if (materiaId == null || materiaId.isBlank()) {
                continue;
            }
            materiasVigentes.add(materiaId);
        }

        if (materiasVigentes.isEmpty()) {
            neo4jClient.query("""
                    MATCH (e:Estudiante {id: $estudianteId})-[r:CURSO]->(:Materia)
                    DELETE r
                    """)
                    .bind(estudianteId).to("estudianteId")
                    .run();
            return;
        }

        neo4jClient.query("""
                MATCH (e:Estudiante {id: $estudianteId})-[r:CURSO]->(m:Materia)
                WHERE NOT m.id IN $materiasVigentes
                DELETE r
                """)
                .bind(estudianteId).to("estudianteId")
                .bind(new ArrayList<>(materiasVigentes)).to("materiasVigentes")
                .run();

        for (MateriaCursoContexto contexto : materiasObjetivo) {
            if (contexto == null || contexto.materia() == null || contexto.institucionId() == null || contexto.institucionId().isBlank()) {
                continue;
            }
            registrarMateriaCursada(estudianteId, contexto.institucionId(), contexto.materia());
        }
    }

    private void registrarMateriaCursada(String estudianteId, String institucionId, Materia historialMateria) {
        if (historialMateria == null || historialMateria.getMateriaId() == null || historialMateria.getMateriaId().isBlank()) {
            return;
        }

        List<String> examenesParciales = historialMateria.getExamenesParciales() != null
                ? historialMateria.getExamenesParciales() : Collections.emptyList();
        List<String> trabajosPracticos = historialMateria.getTrabajosPracticos() != null
                ? historialMateria.getTrabajosPracticos() : Collections.emptyList();
        List<String> examenesFinales = historialMateria.getExamenesFinales() != null
                ? historialMateria.getExamenesFinales() : Collections.emptyList();
        List<String> calificacionesCombinadas = combinarCalificaciones(examenesParciales, trabajosPracticos, examenesFinales);
        Double promedioFinal = resolverPromedioFinal(
                calificacionesCombinadas,
                historialMateria.getPromedioFinal(),
                historialMateria.getFechaAprobacionFinal() != null
        );

        neo4jClient.query("""
                MERGE (m:Materia {id: $materiaId})
                SET m.nombre = coalesce($materiaNombre, m.nombre),
                    m.name = $materiaId,
                    m.anioLectivo = coalesce($anioLectivo, m.anioLectivo)
                """)
                .bind(historialMateria.getMateriaId()).to("materiaId")
                .bind(historialMateria.getMateriaNombre()).to("materiaNombre")
                .bind(historialMateria.getAnioLectivo() != null ? historialMateria.getAnioLectivo() : 0).to("anioLectivo")
                .run();

        neo4jClient.query("""
                MATCH (i:Institucion {id: $institucionId})
                MATCH (m:Materia {id: $materiaId})
                MERGE (i)-[:ENSENA]->(m)
                """)
                .bind(institucionId).to("institucionId")
                .bind(historialMateria.getMateriaId()).to("materiaId")
                .run();

        neo4jClient.query("""
                MATCH (e:Estudiante {id: $estudianteId})
                MATCH (m:Materia {id: $materiaId})
                MERGE (e)-[r:CURSO]->(m)
                SET r.sistemaCalificacion = $sistemaCalificacion,
                    r.examenesParciales = $examenesParciales,
                    r.trabajosPracticos = $trabajosPracticos,
                    r.examenesFinales = $examenesFinales,
                    r.promedioFinal = $promedioFinal,
                    r.fechaAprobacionFinal = $fechaAprobacionFinal,
                    r.institucionId = $institucionId
                """)
                .bind(estudianteId).to("estudianteId")
                .bind(historialMateria.getMateriaId()).to("materiaId")
                .bind(historialMateria.getSistemaCalificacion() != null ? historialMateria.getSistemaCalificacion() : "AR").to("sistemaCalificacion")
                .bind(examenesParciales).to("examenesParciales")
                .bind(trabajosPracticos).to("trabajosPracticos")
                .bind(examenesFinales).to("examenesFinales")
                .bind(promedioFinal != null ? promedioFinal : 0.0).to("promedioFinal")
                .bind(historialMateria.getFechaAprobacionFinal() != null ? historialMateria.getFechaAprobacionFinal().toString() : "").to("fechaAprobacionFinal")
                .bind(institucionId).to("institucionId")
                .run();
    }

    public void asociarInstitucionPrevia(String estudianteId, String institucionId) {
        asociarInstitucionPrevia(estudianteId, institucionId, false);
    }

    public void asociarInstitucionPrevia(String estudianteId, String institucionId, boolean esActual) {
        neo4jClient.query("""
                MERGE (i:Institucion {id: $institucionId})
                SET i.name = coalesce(i.name, i.nombre, i.id)
                """)
                .bind(institucionId).to("institucionId")
                .run();

        if (esActual) {
            neo4jClient.query("""
                    MATCH (e:Estudiante {id: $estudianteId})-[r:ASISTIO_A]->(:Institucion)
                    SET r.esActual = false
                    """)
                    .bind(estudianteId).to("estudianteId")
                    .run();
        }

        estudianteNeo4jRepository.registrarHistorial(estudianteId, institucionId, esActual);
    }

    public void registrarCurso(String estudianteId, String materiaId, String institucionId, String sistemaCalificacion,
                               List<String> examenesParciales, List<String> trabajosPracticos, List<String> examenesFinales,
                               Double promedioFinal,
                               LocalDate fechaAprobacionFinal) {
        Map<String, Object> cursoActual = neo4jClient.query("""
                MATCH (e:Estudiante {id: $estudianteId})-[r:CURSO]->(m:Materia {id: $materiaId})
                RETURN r.fechaAprobacionFinal AS fechaAprobacionFinal, r.promedioFinal AS promedioFinal
                """)
                .bind(estudianteId).to("estudianteId")
                .bind(materiaId).to("materiaId")
                .fetch()
                .one()
                .orElse(Collections.emptyMap());

        boolean finalizadaExistente = cursoActual.get("fechaAprobacionFinal") != null
                && !String.valueOf(cursoActual.get("fechaAprobacionFinal")).isBlank();
        boolean finalizada = fechaAprobacionFinal != null || finalizadaExistente;
        List<String> parciales = examenesParciales != null ? examenesParciales : Collections.emptyList();
        List<String> practicos = trabajosPracticos != null ? trabajosPracticos : Collections.emptyList();
        List<String> finales = examenesFinales != null ? examenesFinales : Collections.emptyList();
        List<String> calificacionesCombinadas = combinarCalificaciones(parciales, practicos, finales);
        Double promedioResuelto = resolverPromedioFinal(calificacionesCombinadas, promedioFinal, finalizada);

        neo4jClient.query("""
                MATCH (e:Estudiante {id: $estudianteId})
                MATCH (m:Materia {id: $materiaId})
                MERGE (e)-[r:CURSO]->(m)
                SET r.sistemaCalificacion = $sistemaCalificacion,
                    r.examenesParciales = $examenesParciales,
                    r.trabajosPracticos = $trabajosPracticos,
                    r.examenesFinales = $examenesFinales,
                    r.promedioFinal = $promedioFinal,
                    r.fechaAprobacionFinal = $fechaAprobacionFinal,
                    r.institucionId = $institucionId
                """)
                .bind(estudianteId).to("estudianteId")
                .bind(materiaId).to("materiaId")
                .bind(institucionId).to("institucionId")
                .bind(sistemaCalificacion != null ? sistemaCalificacion : "AR").to("sistemaCalificacion")
                .bind(parciales).to("examenesParciales")
                .bind(practicos).to("trabajosPracticos")
                .bind(finales).to("examenesFinales")
                .bind(promedioResuelto != null ? promedioResuelto : 0.0).to("promedioFinal")
                .bind(fechaAprobacionFinal != null ? fechaAprobacionFinal.toString() : "").to("fechaAprobacionFinal")
                .run();
    }

    public Map<String, Object> obtenerPromediosSudafricaPorNivel(String estudianteId) {
        Estudiante estudiante = obtenerPorId(estudianteId);

        Map<String, List<Double>> notasPorNivel = new HashMap<>();
        notasPorNivel.put("primaria", new ArrayList<>());
        notasPorNivel.put("secundaria", new ArrayList<>());
        notasPorNivel.put("universidad", new ArrayList<>());

        if (estudiante.getHistorialAcademico() != null) {
            for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                if (historialInstitucion == null || historialInstitucion.getInstitucionId() == null || historialInstitucion.getInstitucionId().isBlank()) {
                    continue;
                }
                String nivel = resolverNivelInstitucion(historialInstitucion.getInstitucionId());
                String pais = resolverPaisInstitucion(historialInstitucion.getInstitucionId(), estudiante.getPaisOrigen());
                acumularNotasPorNivel(notasPorNivel, nivel, pais, historialInstitucion.getMaterias());
            }
        }

        if (estudiante.getInstitucionActualId() != null && !estudiante.getInstitucionActualId().isBlank()) {
            String nivelActual = resolverNivelInstitucion(estudiante.getInstitucionActualId());
            String paisActual = resolverPaisInstitucion(estudiante.getInstitucionActualId(), estudiante.getPaisOrigen());
            acumularNotasPorNivel(notasPorNivel, nivelActual, paisActual, estudiante.getMateriasInstitucionActual());
        }

        Map<String, Object> promedios = new HashMap<>();
        for (String nivel : List.of("primaria", "secundaria", "universidad")) {
            List<Double> notas = notasPorNivel.getOrDefault(nivel, Collections.emptyList());
            Double promedio = null;
            if (!notas.isEmpty()) {
                double suma = 0.0;
                for (Double n : notas) {
                    suma += n;
                }
                promedio = suma / notas.size();
            }

            Map<String, Object> detalle = new HashMap<>();
            detalle.put("promedioSudafrica", promedio);
            detalle.put("materiasConsideradas", notas.size());
            promedios.put(nivel, detalle);
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("estudianteId", estudiante.getId());
        resultado.put("estudianteNombre", estudiante.getNombre());
        resultado.put("promediosPorNivel", promedios);
        return resultado;
    }

    private void acumularNotasPorNivel(Map<String, List<Double>> notasPorNivel,
                                       String nivel,
                                       String pais,
                                       List<Materia> materias) {
        String nivelNormalizado = normalizarNivel(nivel);
        if (!notasPorNivel.containsKey(nivelNormalizado) || materias == null) {
            return;
        }

        for (Materia materia : materias) {
            if (materia == null) {
                continue;
            }

            if ("universidad".equals(nivelNormalizado) && materia.getFechaAprobacionFinal() == null) {
                continue;
            }

            String notaOrigen = resolverNotaOrigenMateria(materia);
            if (notaOrigen == null || notaOrigen.isBlank()) {
                continue;
            }

            LocalDate fechaReferencia = resolverFechaReferencia(materia);
            try {
                Double notaSudafrica = equivalenciaCalificacionService.convertirANotaSudafrica(pais, notaOrigen, fechaReferencia);
                if (notaSudafrica != null) {
                    notasPorNivel.get(nivelNormalizado).add(notaSudafrica);
                }
            } catch (IllegalArgumentException ignored) {
                // Si falta un mapeo para un pais/fecha, esa nota no se considera en el promedio.
            }
        }
    }

    private String resolverNivelInstitucion(String institucionId) {
        Optional<Institucion> institucion = institucionRepository.findById(institucionId);
        if (institucion.isEmpty() || institucion.get().getMetadatos() == null) {
            return "secundaria";
        }
        Object nivel = institucion.get().getMetadatos().get("nivel");
        return nivel != null ? nivel.toString() : "secundaria";
    }

    private String normalizarNivel(String nivel) {
        if (nivel == null) {
            return "secundaria";
        }
        String n = nivel.trim().toLowerCase();
        if (n.contains("prim")) {
            return "primaria";
        }
        if (n.contains("sec")) {
            return "secundaria";
        }
        if (n.contains("uni") || n.contains("terc")) {
            return "universidad";
        }
        return "secundaria";
    }

    private String resolverPaisInstitucion(String institucionId, String paisDefault) {
        Optional<Institucion> institucion = institucionRepository.findById(institucionId);
        if (institucion.isPresent() && institucion.get().getPais() != null && !institucion.get().getPais().isBlank()) {
            return institucion.get().getPais();
        }
        return paisDefault;
    }

    private String resolverNotaOrigenMateria(Materia materia) {
        if (materia.getPromedioFinal() != null) {
            return String.valueOf(materia.getPromedioFinal());
        }

        if (materia.getExamenesFinales() != null && !materia.getExamenesFinales().isEmpty()) {
            for (int i = materia.getExamenesFinales().size() - 1; i >= 0; i--) {
                String nota = materia.getExamenesFinales().get(i);
                if (nota != null && !nota.isBlank()) {
                    return nota;
                }
            }
        }

        List<String> combinadas = combinarCalificaciones(
                materia.getExamenesParciales(),
                materia.getTrabajosPracticos(),
                materia.getExamenesFinales()
        );
        for (int i = combinadas.size() - 1; i >= 0; i--) {
            String nota = combinadas.get(i);
            if (nota != null && !nota.isBlank()) {
                return nota;
            }
        }

        return null;
    }

    private LocalDate resolverFechaReferencia(Materia materia) {
        if (materia.getFechaAprobacionFinal() != null) {
            return materia.getFechaAprobacionFinal();
        }
        if (materia.getAnioLectivo() != null) {
            return LocalDate.of(materia.getAnioLectivo(), 12, 31);
        }
        return LocalDate.now();
    }
    public Map<String, Object> obtenerReporteTrayectoriaOrdenada(String estudianteId) {
        Estudiante estudiante = obtenerPorId(estudianteId);
        Map<String, InstitucionTrayectoria> instituciones = new HashMap<>();

        if (estudiante.getHistorialAcademico() != null) {
            for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                if (historialInstitucion == null || historialInstitucion.getInstitucionId() == null || historialInstitucion.getInstitucionId().isBlank()) {
                    continue;
                }
                InstitucionTrayectoria bloque = instituciones.computeIfAbsent(
                        historialInstitucion.getInstitucionId(),
                        id -> new InstitucionTrayectoria(
                                historialInstitucion.getInstitucionId(),
                                historialInstitucion.getInstitucionNombre() != null ? historialInstitucion.getInstitucionNombre() : historialInstitucion.getInstitucionId()
                        )
                );

                if (historialInstitucion.getMaterias() != null) {
                    for (Materia materia : historialInstitucion.getMaterias()) {
                        agregarFilaMateria(bloque, materia);
                    }
                }
            }
        }

        if (estudiante.getInstitucionActualId() != null && !estudiante.getInstitucionActualId().isBlank()) {
            InstitucionTrayectoria bloqueActual = instituciones.computeIfAbsent(
                    estudiante.getInstitucionActualId(),
                    id -> new InstitucionTrayectoria(estudiante.getInstitucionActualId(), estudiante.getInstitucionActualId())
            );
            bloqueActual.setInstitucionActual(true);

            if (estudiante.getMateriasInstitucionActual() != null) {
                for (Materia materiaActual : estudiante.getMateriasInstitucionActual()) {
                    agregarFilaMateria(bloqueActual, materiaActual);
                }
            }
        }

        List<InstitucionTrayectoria> secciones = new ArrayList<>(instituciones.values());
        secciones.sort(
                Comparator.comparing(InstitucionTrayectoria::isInstitucionActual, Comparator.reverseOrder())
                        .thenComparing(
                                InstitucionTrayectoria::getUltimaFechaFinal,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                        .thenComparing(InstitucionTrayectoria::getInstitucionNombre, Comparator.nullsLast(String::compareToIgnoreCase))
        );

        for (InstitucionTrayectoria seccion : secciones) {
            seccion.getTablaMaterias().sort(
                    Comparator.comparing(FilaMateriaReporte::getAnioMateria, Comparator.nullsLast(Integer::compareTo))
                            .thenComparing(FilaMateriaReporte::getNombreMateria, Comparator.nullsLast(String::compareToIgnoreCase))
            );
        }

        List<Map<String, Object>> reporte = new ArrayList<>();
        for (InstitucionTrayectoria seccion : secciones) {
            Map<String, Object> bloque = new HashMap<>();
            bloque.put("institucionId", seccion.getInstitucionId());
            bloque.put("institucionNombre", seccion.getInstitucionNombre());
            bloque.put("ultimaFechaFinal", seccion.getUltimaFechaFinal());
            bloque.put("tablaMaterias", seccion.getTablaMaterias());
            reporte.add(bloque);
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("estudianteId", estudiante.getId());
        resultado.put("estudianteNombre", estudiante.getNombre());
        resultado.put("reporteTrayectoria", reporte);
        return resultado;
    }

    private void agregarFilaMateria(InstitucionTrayectoria bloque, Materia materia) {
        if (materia == null) {
            return;
        }
        LocalDate fechaAprobacion = materia.getFechaAprobacionFinal();
        boolean enCurso = fechaAprobacion == null;

        String fechaTexto = enCurso ? "En curso" : fechaAprobacion.toString();
        String notaFinalTexto = enCurso || materia.getPromedioFinal() == null ? "N/A" : String.valueOf(materia.getPromedioFinal());

        FilaMateriaReporte fila = new FilaMateriaReporte(
                materia.getAnioLectivo(),
                materia.getMateriaNombre(),
                fechaTexto,
                notaFinalTexto
        );
        bloque.getTablaMaterias().add(fila);

        if (!enCurso) {
            if (bloque.getUltimaFechaFinal() == null || fechaAprobacion.isAfter(bloque.getUltimaFechaFinal())) {
                bloque.setUltimaFechaFinal(fechaAprobacion);
            }
        }
    }

    private static class InstitucionTrayectoria {
        private final String institucionId;
        private final String institucionNombre;
        private boolean institucionActual;
        private LocalDate ultimaFechaFinal;
        private final List<FilaMateriaReporte> tablaMaterias = new ArrayList<>();

        private InstitucionTrayectoria(String institucionId, String institucionNombre) {
            this.institucionId = institucionId;
            this.institucionNombre = institucionNombre;
        }

        public String getInstitucionId() {
            return institucionId;
        }

        public String getInstitucionNombre() {
            return institucionNombre;
        }

        public boolean isInstitucionActual() {
            return institucionActual;
        }

        public void setInstitucionActual(boolean institucionActual) {
            this.institucionActual = institucionActual;
        }

        public LocalDate getUltimaFechaFinal() {
            return ultimaFechaFinal;
        }

        public void setUltimaFechaFinal(LocalDate ultimaFechaFinal) {
            this.ultimaFechaFinal = ultimaFechaFinal;
        }

        public List<FilaMateriaReporte> getTablaMaterias() {
            return tablaMaterias;
        }
    }

    private static class FilaMateriaReporte {
        private final Integer anioMateria;
        private final String nombreMateria;
        private final String fechaAprobacion;
        private final String notaFinal;

        private FilaMateriaReporte(Integer anioMateria, String nombreMateria, String fechaAprobacion, String notaFinal) {
            this.anioMateria = anioMateria;
            this.nombreMateria = nombreMateria;
            this.fechaAprobacion = fechaAprobacion;
            this.notaFinal = notaFinal;
        }

        public Integer getAnioMateria() {
            return anioMateria;
        }

        public String getNombreMateria() {
            return nombreMateria;
        }

        public String getFechaAprobacion() {
            return fechaAprobacion;
        }

        public String getNotaFinal() {
            return notaFinal;
        }
    }

    private List<String> combinarCalificaciones(List<String> examenesParciales, List<String> trabajosPracticos, List<String> examenesFinales) {
        List<String> combinadas = new ArrayList<>();
        if (examenesParciales != null) {
            combinadas.addAll(examenesParciales);
        }
        if (trabajosPracticos != null) {
            combinadas.addAll(trabajosPracticos);
        }
        if (examenesFinales != null) {
            combinadas.addAll(examenesFinales);
        }
        return combinadas;
    }

    private Double resolverPromedioFinal(List<String> calificaciones, Double promedioFinalRecibido, boolean materiaFinalizada) {
        if (!materiaFinalizada || calificaciones == null || calificaciones.isEmpty()) {
            return promedioFinalRecibido;
        }

        double suma = 0.0;
        int cantidad = 0;
        for (String valor : calificaciones) {
            if (valor == null || valor.isBlank()) {
                continue;
            }
            try {
                suma += Double.parseDouble(valor.trim());
                cantidad++;
            } catch (NumberFormatException ignored) {
            }
        }

        if (cantidad == 0) {
            return promedioFinalRecibido;
        }
        return suma / cantidad;
    }

    private record MateriaCursoContexto(String institucionId, Materia materia) {
    }

}




