package com.example.demo.service;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestRegistrarCalificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalificacionService {
    @Autowired
    private EquivalenciaCalificacionService equivalenciaCalificacionService;
    @Autowired
    private EstudianteService estudianteService;

    public Estudiante registrarCalificacionOriginal(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        if (requestRegistrarCalificacion == null) {
            throw new IllegalArgumentException("La calificacion es obligatoria.");
        }
        if (requestRegistrarCalificacion.getEstudiante() == null || requestRegistrarCalificacion.getEstudiante().isBlank()) {
            throw new IllegalArgumentException("El id de estudiante es obligatorio.");
        }
        if (requestRegistrarCalificacion.getMateria() == null || requestRegistrarCalificacion.getMateria().isBlank()) {
            throw new IllegalArgumentException("El id de materia es obligatorio.");
        }

        Estudiante estudiante = estudianteService.obtenerPorId(requestRegistrarCalificacion.getEstudiante());
        Materia materiaEstudiante = obtenerOCrearMateria(estudiante, requestRegistrarCalificacion);
        aplicarNuevaCalificacion(materiaEstudiante, requestRegistrarCalificacion);
        return estudianteService.actualizarEstudiante(estudiante.getId(), estudiante);
    }

    public Estudiante actualizarCalificacion(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        if (requestRegistrarCalificacion == null) {
            throw new IllegalArgumentException("La calificacion es obligatoria.");
        }
        if (requestRegistrarCalificacion.getEstudiante() == null || requestRegistrarCalificacion.getEstudiante().isBlank()) {
            throw new IllegalArgumentException("El id de estudiante es obligatorio.");
        }
        if (requestRegistrarCalificacion.getInstitucion() == null || requestRegistrarCalificacion.getInstitucion().isBlank()) {
            throw new IllegalArgumentException("El id de institucion es obligatorio.");
        }
        if (requestRegistrarCalificacion.getMateria() == null || requestRegistrarCalificacion.getMateria().isBlank()) {
            throw new IllegalArgumentException("El id de materia es obligatorio.");
        }

        Estudiante estudiante = estudianteService.obtenerPorId(requestRegistrarCalificacion.getEstudiante());
        Materia materiaEstudiante = obtenerMateriaExistente(estudiante, requestRegistrarCalificacion);
        aplicarActualizacionCalificacion(materiaEstudiante, requestRegistrarCalificacion);
        return estudianteService.actualizarEstudiante(estudiante.getId(), estudiante);
    }

    public Estudiante eliminarCalificacion(String estudianteId, String institucionId, String materiaId) {
        if (estudianteId == null || estudianteId.isBlank()) {
            throw new IllegalArgumentException("El id de estudiante es obligatorio.");
        }
        if (institucionId == null || institucionId.isBlank()) {
            throw new IllegalArgumentException("El id de institucion es obligatorio.");
        }
        if (materiaId == null || materiaId.isBlank()) {
            throw new IllegalArgumentException("El id de materia es obligatorio.");
        }

        Estudiante estudiante = estudianteService.obtenerPorId(estudianteId);
        removerMateriaDeEstudiante(estudiante, materiaId, institucionId);
        return estudianteService.actualizarEstudiante(estudiante.getId(), estudiante);
    }

    public String calcularConversionSudafrica(RequestRegistrarCalificacion request) {
        if (request == null) {
            return "SIN_DATOS";
        }

        String notaOrigen = resolverNotaOriginal(request);
        if (notaOrigen == null || notaOrigen.isBlank()) {
            return "SIN_DATOS";
        }

        LocalDate fechaReferencia = resolverFechaReferencia(request);
        Double conversion = equivalenciaCalificacionService.convertirANotaSudafrica(
                request.getPaisOrigen(),
                notaOrigen,
                fechaReferencia
        );
        if (conversion == null) {
            return "SIN_DATOS";
        }

        return conversion + "%";
    }

    private String resolverNotaOriginal(RequestRegistrarCalificacion request) {
        if (request.getPromedioFinal() != null) {
            return String.valueOf(request.getPromedioFinal());
        }

        List<String> finales = request.getExamenesFinales() != null ? request.getExamenesFinales() : List.of();
        for (int i = finales.size() - 1; i >= 0; i--) {
            String valor = finales.get(i);
            if (valor != null && !valor.isBlank()) {
                return valor;
            }
        }

        List<String> combinadas = new ArrayList<>();
        if (request.getExamenesParciales() != null) {
            combinadas.addAll(request.getExamenesParciales());
        }
        if (request.getTrabajosPracticos() != null) {
            combinadas.addAll(request.getTrabajosPracticos());
        }
        for (int i = combinadas.size() - 1; i >= 0; i--) {
            String valor = combinadas.get(i);
            if (valor != null && !valor.isBlank()) {
                return valor;
            }
        }

        return null;
    }

    private LocalDate resolverFechaReferencia(RequestRegistrarCalificacion request) {
        if (request.getFechaAprobacionFinal() != null) {
            return request.getFechaAprobacionFinal();
        }
        if (request.getAnioLectivo() != null) {
            return LocalDate.of(request.getAnioLectivo(), 12, 31);
        }
        return LocalDate.now();
    }

    private Materia obtenerMateriaExistente(Estudiante estudiante, RequestRegistrarCalificacion request) {
        String institucionId = request.getInstitucion();
        String materiaId = request.getMateria();

        if (institucionId != null && !institucionId.isBlank()) {
            if (institucionId.equals(estudiante.getInstitucionActualId())) {
                Materia materiaActual = buscarMateriaPorId(estudiante.getMateriasInstitucionActual(), materiaId);
                if (materiaActual != null) {
                    return materiaActual;
                }
            }

            if (estudiante.getHistorialAcademico() != null) {
                for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                    if (historialInstitucion == null || historialInstitucion.getInstitucionId() == null) {
                        continue;
                    }
                    if (!institucionId.equals(historialInstitucion.getInstitucionId())) {
                        continue;
                    }
                    Materia materiaHistorial = buscarMateriaPorId(historialInstitucion.getMaterias(), materiaId);
                    if (materiaHistorial != null) {
                        return materiaHistorial;
                    }
                }
            }
        }

        Materia materiaActual = buscarMateriaPorId(estudiante.getMateriasInstitucionActual(), materiaId);
        if (materiaActual != null) {
            return materiaActual;
        }

        if (estudiante.getHistorialAcademico() != null) {
            for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                Materia materiaHistorial = buscarMateriaPorId(historialInstitucion != null ? historialInstitucion.getMaterias() : null, materiaId);
                if (materiaHistorial != null) {
                    return materiaHistorial;
                }
            }
        }

        throw new IllegalArgumentException("La materia " + materiaId + " no existe en el estudiante " + estudiante.getId());
    }

    private Materia obtenerOCrearMateria(Estudiante estudiante, RequestRegistrarCalificacion request) {
        try {
            return obtenerMateriaExistente(estudiante, request);
        } catch (IllegalArgumentException ignored) {
            // Si no existe, se crea en la institucion indicada.
        }

        String materiaId = request.getMateria();
        String institucionId = request.getInstitucion();

        Materia nuevaMateria = new Materia();
        nuevaMateria.setMateriaId(materiaId);
        nuevaMateria.setMateriaNombre(materiaId);
        nuevaMateria.setAnioLectivo(request.getAnioLectivo());
        nuevaMateria.setSistemaCalificacion(request.getSistemaCalificacion());
        nuevaMateria.setExamenesParciales(new ArrayList<>());
        nuevaMateria.setTrabajosPracticos(new ArrayList<>());
        nuevaMateria.setExamenesFinales(new ArrayList<>());

        if (institucionId != null && !institucionId.isBlank()) {
            if (institucionId.equals(estudiante.getInstitucionActualId())) {
                if (estudiante.getMateriasInstitucionActual() == null) {
                    estudiante.setMateriasInstitucionActual(new ArrayList<>());
                }
                estudiante.getMateriasInstitucionActual().add(nuevaMateria);
                return nuevaMateria;
            }

            if (estudiante.getHistorialAcademico() == null) {
                estudiante.setHistorialAcademico(new ArrayList<>());
            }
            for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                if (historialInstitucion == null || historialInstitucion.getInstitucionId() == null) {
                    continue;
                }
                if (institucionId.equals(historialInstitucion.getInstitucionId())) {
                    if (historialInstitucion.getMaterias() == null) {
                        historialInstitucion.setMaterias(new ArrayList<>());
                    }
                    historialInstitucion.getMaterias().add(nuevaMateria);
                    return nuevaMateria;
                }
            }

            Estudiante.HistorialInstitucion nuevaHistorial = new Estudiante.HistorialInstitucion();
            nuevaHistorial.setInstitucionId(institucionId);
            nuevaHistorial.setInstitucionNombre(institucionId);
            nuevaHistorial.setMaterias(new ArrayList<>(List.of(nuevaMateria)));
            estudiante.getHistorialAcademico().add(nuevaHistorial);
            return nuevaMateria;
        }

        if (estudiante.getMateriasInstitucionActual() == null) {
            estudiante.setMateriasInstitucionActual(new ArrayList<>());
        }
        estudiante.getMateriasInstitucionActual().add(nuevaMateria);
        return nuevaMateria;
    }

    private Materia buscarMateriaPorId(List<Materia> materias, String materiaId) {
        if (materias == null || materiaId == null || materiaId.isBlank()) {
            return null;
        }
        for (Materia materia : materias) {
            if (materia != null && materiaId.equals(materia.getMateriaId())) {
                return materia;
            }
        }
        return null;
    }

    private void aplicarNuevaCalificacion(Materia materia, RequestRegistrarCalificacion request) {
        if (materia.getExamenesParciales() == null) {
            materia.setExamenesParciales(new ArrayList<>());
        }
        if (materia.getTrabajosPracticos() == null) {
            materia.setTrabajosPracticos(new ArrayList<>());
        }

        agregarNotas(materia.getExamenesParciales(), request.getExamenesParciales());
        agregarNotas(materia.getTrabajosPracticos(), request.getTrabajosPracticos());

        String nuevaNotaFinal = ultimaNotaValida(request.getExamenesFinales());
        if (nuevaNotaFinal != null) {
            materia.setExamenesFinales(new ArrayList<>(List.of(nuevaNotaFinal)));
        }

        if (request.getPromedioFinal() != null) {
            materia.setPromedioFinal(request.getPromedioFinal());
        }
        if (request.getFechaAprobacionFinal() != null) {
            materia.setFechaAprobacionFinal(request.getFechaAprobacionFinal());
        }
        if (request.getSistemaCalificacion() != null && !request.getSistemaCalificacion().isBlank()) {
            materia.setSistemaCalificacion(request.getSistemaCalificacion());
        }
        if (request.getAnioLectivo() != null) {
            materia.setAnioLectivo(request.getAnioLectivo());
        }
    }

    private void aplicarActualizacionCalificacion(Materia materia, RequestRegistrarCalificacion request) {
        materia.setExamenesParciales(limpiarNotas(request.getExamenesParciales()));
        materia.setTrabajosPracticos(limpiarNotas(request.getTrabajosPracticos()));

        String nuevaNotaFinal = ultimaNotaValida(request.getExamenesFinales());
        if (nuevaNotaFinal != null) {
            materia.setExamenesFinales(new ArrayList<>(List.of(nuevaNotaFinal)));
        } else {
            materia.setExamenesFinales(new ArrayList<>());
        }

        materia.setPromedioFinal(request.getPromedioFinal());
        materia.setFechaAprobacionFinal(request.getFechaAprobacionFinal());
        if (request.getSistemaCalificacion() != null && !request.getSistemaCalificacion().isBlank()) {
            materia.setSistemaCalificacion(request.getSistemaCalificacion());
        }
        if (request.getAnioLectivo() != null) {
            materia.setAnioLectivo(request.getAnioLectivo());
        }
    }

    private List<String> limpiarNotas(List<String> notas) {
        List<String> limpias = new ArrayList<>();
        if (notas == null) {
            return limpias;
        }
        for (String nota : notas) {
            if (nota != null && !nota.isBlank()) {
                limpias.add(nota.trim());
            }
        }
        return limpias;
    }

    private void agregarNotas(List<String> destino, List<String> nuevas) {
        if (destino == null || nuevas == null || nuevas.isEmpty()) {
            return;
        }
        for (String nota : nuevas) {
            if (nota != null && !nota.isBlank()) {
                destino.add(nota.trim());
            }
        }
    }

    private String ultimaNotaValida(List<String> notas) {
        if (notas == null || notas.isEmpty()) {
            return null;
        }
        for (int i = notas.size() - 1; i >= 0; i--) {
            String nota = notas.get(i);
            if (nota != null && !nota.isBlank()) {
                return nota.trim();
            }
        }
        return null;
    }

    private void removerMateriaDeEstudiante(Estudiante estudiante, String materiaId, String institucionId) {
        if (institucionId != null && !institucionId.isBlank()) {
            if (institucionId.equals(estudiante.getInstitucionActualId())) {
                removerMateriaDeLista(estudiante.getMateriasInstitucionActual(), materiaId);
                return;
            }

            if (estudiante.getHistorialAcademico() != null) {
                for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                    if (historialInstitucion == null || historialInstitucion.getInstitucionId() == null) {
                        continue;
                    }
                    if (institucionId.equals(historialInstitucion.getInstitucionId())) {
                        removerMateriaDeLista(historialInstitucion.getMaterias(), materiaId);
                        return;
                    }
                }
            }
        }

        removerMateriaDeLista(estudiante.getMateriasInstitucionActual(), materiaId);
        if (estudiante.getHistorialAcademico() != null) {
            for (Estudiante.HistorialInstitucion historialInstitucion : estudiante.getHistorialAcademico()) {
                removerMateriaDeLista(historialInstitucion != null ? historialInstitucion.getMaterias() : null, materiaId);
            }
        }
    }

    private void removerMateriaDeLista(List<Materia> materias, String materiaId) {
        if (materias == null || materiaId == null || materiaId.isBlank()) {
            return;
        }
        materias.removeIf(m -> m != null && materiaId.equals(m.getMateriaId()));
    }
}
