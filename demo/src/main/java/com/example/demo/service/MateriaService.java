package com.example.demo.service;

import com.example.demo.model.CursoMateria;
import com.example.demo.model.Estudiante;
import com.example.demo.model.Institucion;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestAsignarMateriaEstudiante;
import com.example.demo.model.RequestModificarMateria;
import com.example.demo.model.RequestRegistrarMateria;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {

    @Autowired
    private MateriaNeo4jRepository materiaRepository;
    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;
    @Autowired
    private EstudianteMONGORepository estudianteMongoRepository;
    @Autowired
    private InstitucionNeo4jRepository institucionNeo4jRepository;
    @Autowired
    private InstitucionMONGORepository institucionMongoRepository;

    public Materia registrarMateria(RequestRegistrarMateria requestRegistrarMateria) {
        if (requestRegistrarMateria.getInstitucionId() == null || requestRegistrarMateria.getInstitucionId().isBlank()) {
            throw new IllegalArgumentException("institucionId es obligatorio.");
        }
        Optional<Institucion> institucionNeo4jOpt = institucionNeo4jRepository.findById(requestRegistrarMateria.getInstitucionId());
        Optional<Institucion> institucionMongoOpt = institucionMongoRepository.findById(requestRegistrarMateria.getInstitucionId());
        if (institucionNeo4jOpt.isEmpty() || institucionMongoOpt.isEmpty()) {
            throw new IllegalArgumentException("La institucion indicada no existe.");
        }

        Materia m = new Materia();
        m.setId(requestRegistrarMateria.getCodigo());
        m.setNombre(requestRegistrarMateria.getNombre());
        Materia guardada = materiaRepository.save(m);

        Institucion institucionNeo4j = institucionNeo4jOpt.get();
        Institucion institucionMongo = institucionMongoOpt.get();
        if (institucionNeo4j.getCurriculum() == null) {
            institucionNeo4j.setCurriculum(new ArrayList<>());
        }
        if (institucionMongo.getCurriculum() == null) {
            institucionMongo.setCurriculum(new ArrayList<>());
        }

        Institucion.Curso curso = new Institucion.Curso(guardada.getId(), guardada.getNombre());
        upsertCurso(institucionNeo4j.getCurriculum(), curso);
        upsertCurso(institucionMongo.getCurriculum(), curso);

        institucionNeo4jRepository.save(institucionNeo4j);
        institucionMongoRepository.save(institucionMongo);
        return guardada;
    }

    public Materia crearMateriaYAsignarAEstudiante(String idEstudiante, RequestAsignarMateriaEstudiante request) {
        Optional<Estudiante> estudianteNeo4jOpt = estudianteNeo4jRepository.findById(idEstudiante);
        Optional<Estudiante> estudianteMongoOpt = estudianteMongoRepository.findById(idEstudiante);
        if (estudianteNeo4jOpt.isEmpty() || estudianteMongoOpt.isEmpty()) {
            return null;
        }

        Materia materia = materiaRepository.findById(request.getCodigo()).orElseGet(Materia::new);
        materia.setId(request.getCodigo());
        materia.setNombre(request.getNombre());
        materia.setFechaFinalizacion(request.getFechaFinalizacion());
        materia.setNotaFinal(request.getNotaFinal());
        materia.setNotasParciales(request.getNotasParciales());
        Materia guardada = materiaRepository.save(materia);

        String fechaRendida = request.getFechaRendida() == null ? null : request.getFechaRendida().toString();
        Double notaRelacion = request.getNotaFinal() == null ? 0.0 : request.getNotaFinal();
        estudianteNeo4jRepository.registrarCursada(idEstudiante, guardada.getId(), notaRelacion, "N/A", "N/A", fechaRendida);

        Estudiante estudianteMongo = estudianteMongoOpt.get();
        if (estudianteMongo.getMaterias() == null) {
            estudianteMongo.setMaterias(new ArrayList<>());
        }
        CursoMateria relacion = new CursoMateria();
        relacion.setMateria(guardada);
        relacion.setNotaFinal(notaRelacion);
        relacion.setFechaRendida(request.getFechaRendida());
        relacion.setPromedio(notaRelacion);
        upsertRelacionMateria(estudianteMongo.getMaterias(), relacion);
        estudianteMongoRepository.save(estudianteMongo);
        return guardada;
    }

    public Materia modificarMateria(String idMateria, RequestModificarMateria request) {
        Optional<Materia> materiaOpt = materiaRepository.findById(idMateria);
        if (materiaOpt.isEmpty()) {
            return null;
        }
        Materia materia = materiaOpt.get();
        if (request.getNombre() != null) {
            materia.setNombre(request.getNombre());
        }
        if (request.getFechaFinalizacion() != null) {
            materia.setFechaFinalizacion(request.getFechaFinalizacion());
        }
        if (request.getNotaFinal() != null) {
            materia.setNotaFinal(request.getNotaFinal());
        }
        if (request.getNotasParciales() != null) {
            materia.setNotasParciales(request.getNotasParciales());
        }
        Materia guardada = materiaRepository.save(materia);

        List<Estudiante> estudiantesMongo = estudianteMongoRepository.findAll();
        for (Estudiante estudiante : estudiantesMongo) {
            if (estudiante.getMaterias() == null) {
                continue;
            }
            boolean actualizado = false;
            for (CursoMateria cursoMateria : estudiante.getMaterias()) {
                if (cursoMateria.getMateria() != null && idMateria.equals(cursoMateria.getMateria().getId())) {
                    cursoMateria.setMateria(guardada);
                    actualizado = true;
                }
            }
            if (actualizado) {
                estudianteMongoRepository.save(estudiante);
            }
        }

        return guardada;
    }

    public boolean eliminarMateriaDeEstudiante(String idEstudiante, String idMateria) {
        Optional<Estudiante> estudianteNeo4jOpt = estudianteNeo4jRepository.findById(idEstudiante);
        Optional<Estudiante> estudianteMongoOpt = estudianteMongoRepository.findById(idEstudiante);
        if (estudianteNeo4jOpt.isEmpty() || estudianteMongoOpt.isEmpty()) {
            return false;
        }
        estudianteNeo4jRepository.eliminarMateriaDeEstudiante(idEstudiante, idMateria);

        Estudiante estudianteMongo = estudianteMongoOpt.get();
        if (estudianteMongo.getMaterias() != null) {
            estudianteMongo.getMaterias().removeIf(c -> c.getMateria() != null && idMateria.equals(c.getMateria().getId()));
            estudianteMongoRepository.save(estudianteMongo);
        }
        return true;
    }

    private void upsertRelacionMateria(List<CursoMateria> materias, CursoMateria nueva) {
        for (int i = 0; i < materias.size(); i++) {
            CursoMateria actual = materias.get(i);
            if (actual.getMateria() != null && nueva.getMateria() != null
                    && nueva.getMateria().getId().equals(actual.getMateria().getId())) {
                materias.set(i, nueva);
                return;
            }
        }
        materias.add(nueva);
    }

    private void upsertCurso(List<Institucion.Curso> cursos, Institucion.Curso nuevo) {
        for (int i = 0; i < cursos.size(); i++) {
            Institucion.Curso actual = cursos.get(i);
            if (actual != null && actual.getId() != null && actual.getId().equals(nuevo.getId())) {
                cursos.set(i, nuevo);
                return;
            }
        }
        cursos.add(nuevo);
    }
}
