package com.example.demo.service;

import com.example.demo.model.CalificacionCassandra;
import com.example.demo.model.Reporte;
import com.example.demo.model.ReporteInstitucionRanking;
import com.example.demo.model.ReporteNivelEducativoRanking;
import com.example.demo.model.ReporteProvinciaRanking;
import com.example.demo.model.ReportePromedio;
import com.example.demo.repository.cassandra.CalificacionCassandraRepository;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class ReporteServicio {

        @Autowired
        private EstudianteNeo4jRepository neo4jRepo;

        @Autowired
        private ReporteCassandraRepository cassandraRepo;
        @Autowired
        private CalificacionCassandraRepository calificacionCassandraRepository;

        // Calcula promedios desde Neo4j y los persiste en Cassandra para reportes.
        public void sincronizarDatosAnaliticos() {
            List<ReportePromedio>promediosPais = neo4jRepo.calcularpromedioPorPais();
            List<ReportePromedio>promediosInst = neo4jRepo.calcularPromedioPorInstitucion();

            List<Reporte> batch = new ArrayList<>();
            promediosPais.forEach(m -> {
                batch.add(new Reporte("PAIS", m.getPromedio(),  m.getPais()));
            });

            promediosInst.forEach(m -> {
                batch.add(new Reporte("INSTITUTO",  m.getPromedio(),m.getNombre()));
            });

            cassandraRepo.saveAll(batch);
        }

        // Genera ranking de instituciones sudafricanas por promedio de conversion.
        public List<ReporteInstitucionRanking> obtenerRankingInstitucionesDesdeCalificaciones() {
            List<CalificacionCassandra> filas = calificacionCassandraRepository.obtenerDatosParaRankingInstituciones();
            Map<String, AcumuladorInstitucion> acumuladores = new HashMap<>();

            for (CalificacionCassandra fila : filas) {
                if (fila.getInstitucionId() == null
                        || fila.getNotaConversionSudafrica() == null
                        || !esSudafrica(fila.getInstitucionPais())) {
                    continue;
                }
                AcumuladorInstitucion acc = acumuladores.computeIfAbsent(
                        fila.getInstitucionId(),
                        k -> new AcumuladorInstitucion(
                                fila.getInstitucionId(),
                                fila.getInstitucionNombre() == null ? fila.getInstitucionId() : fila.getInstitucionNombre(),
                                fila.getInstitucionProvincia()
                        )
                );
                acc.suma += fila.getNotaConversionSudafrica();
                acc.cantidadNotas += 1;
                if (fila.getEstudianteId() != null) {
                    acc.estudiantesUnicos.add(fila.getEstudianteId());
                }
            }

            return acumuladores.values().stream()
                    .filter(a -> a.cantidadNotas > 0)
                    .map(a -> new ReporteInstitucionRanking(
                            a.institucionId,
                            a.institucionNombre,
                            a.institucionProvincia,
                            a.suma / a.cantidadNotas,
                            (long) a.estudiantesUnicos.size()
                    ))
                    .sorted((a, b) -> Double.compare(b.getPromedioConversionSudafrica(), a.getPromedioConversionSudafrica()))
                    .collect(Collectors.toList());
        }

        // Genera ranking de provincias sudafricanas por promedio de conversion.
        public List<ReporteProvinciaRanking> obtenerRankingProvinciasSudafricaDesdeCalificaciones() {
            List<CalificacionCassandra> filas = calificacionCassandraRepository.obtenerDatosParaRankingInstituciones();
            Map<String, AcumuladorProvincia> acumuladores = new HashMap<>();

            for (CalificacionCassandra fila : filas) {
                if (fila.getNotaConversionSudafrica() == null || !esSudafrica(fila.getInstitucionPais())) {
                    continue;
                }
                String provincia = fila.getInstitucionProvincia();
                if (provincia == null || provincia.isBlank()) {
                    continue;
                }
                AcumuladorProvincia acc = acumuladores.computeIfAbsent(provincia, AcumuladorProvincia::new);
                acc.suma += fila.getNotaConversionSudafrica();
                acc.cantidadNotas += 1;
                if (fila.getEstudianteId() != null) {
                    acc.estudiantesUnicos.add(fila.getEstudianteId());
                }
            }

            return acumuladores.values().stream()
                    .filter(a -> a.cantidadNotas > 0)
                    .map(a -> new ReporteProvinciaRanking(
                            a.provincia,
                            a.suma / a.cantidadNotas,
                            (long) a.estudiantesUnicos.size()
                    ))
                    .sorted((a, b) -> Double.compare(b.getPromedioConversionSudafrica(), a.getPromedioConversionSudafrica()))
                    .collect(Collectors.toList());
        }

        // Genera ranking de niveles educativos sudafricanos por promedio de conversion.
        public List<ReporteNivelEducativoRanking> obtenerRankingNivelEducativoSudafricaDesdeCalificaciones() {
            List<CalificacionCassandra> filas = calificacionCassandraRepository.obtenerDatosParaRankingInstituciones();
            Map<String, AcumuladorNivelEducativo> acumuladores = new HashMap<>();

            for (CalificacionCassandra fila : filas) {
                if (fila.getNotaConversionSudafrica() == null || !esSudafrica(fila.getInstitucionPais())) {
                    continue;
                }
                String nivel = fila.getInstitucionNivelEducativo();
                if (nivel == null || nivel.isBlank()) {
                    continue;
                }

                AcumuladorNivelEducativo acc = acumuladores.computeIfAbsent(nivel, AcumuladorNivelEducativo::new);
                acc.suma += fila.getNotaConversionSudafrica();
                acc.cantidadNotas += 1;
                if (fila.getEstudianteId() != null) {
                    acc.estudiantesUnicos.add(fila.getEstudianteId());
                }
            }

            return acumuladores.values().stream()
                    .filter(a -> a.cantidadNotas > 0)
                    .map(a -> new ReporteNivelEducativoRanking(
                            a.nivelEducativo,
                            a.suma / a.cantidadNotas,
                            (long) a.estudiantesUnicos.size()
                    ))
                    .sorted((a, b) -> Double.compare(b.getPromedioConversionSudafrica(), a.getPromedioConversionSudafrica()))
                    .collect(Collectors.toList());
        }

        // Normaliza y valida si un pais corresponde a Sudafrica.
        private boolean esSudafrica(String pais) {
            if (pais == null) {
                return false;
            }
            String normalizado = pais.trim().toLowerCase();
            return "sudafrica".equals(normalizado)
                    || "sudáfrica".equals(normalizado)
                    || "south africa".equals(normalizado);
        }

        // Acumula totales por institucion para calcular promedio y alumnos unicos.
        private static class AcumuladorInstitucion {
            private final String institucionId;
            private final String institucionNombre;
            private final String institucionProvincia;
            private double suma;
            private long cantidadNotas;
            private final Set<String> estudiantesUnicos = new HashSet<>();

            // Inicializa el acumulador por institucion.
            private AcumuladorInstitucion(String institucionId, String institucionNombre, String institucionProvincia) {
                this.institucionId = institucionId;
                this.institucionNombre = institucionNombre;
                this.institucionProvincia = institucionProvincia;
            }
        }

        // Acumula totales por provincia para calcular promedio y alumnos unicos.
        private static class AcumuladorProvincia {
            private final String provincia;
            private double suma;
            private long cantidadNotas;
            private final Set<String> estudiantesUnicos = new HashSet<>();

            // Inicializa el acumulador por provincia.
            private AcumuladorProvincia(String provincia) {
                this.provincia = provincia;
            }
        }

        // Acumula totales por nivel educativo para calcular promedio y alumnos unicos.
        private static class AcumuladorNivelEducativo {
            private final String nivelEducativo;
            private double suma;
            private long cantidadNotas;
            private final Set<String> estudiantesUnicos = new HashSet<>();

            // Inicializa el acumulador por nivel educativo.
            private AcumuladorNivelEducativo(String nivelEducativo) {
                this.nivelEducativo = nivelEducativo;
            }
        }
    }
