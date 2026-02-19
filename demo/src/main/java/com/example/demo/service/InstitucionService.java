package com.example.demo.service;
import com.example.demo.model.Institucion;
import com.example.demo.model.RequestCurso;
import com.example.demo.model.RequestModificarCurso;
import com.example.demo.model.RequestModificarInstitucion;
import com.example.demo.model.RequestRegistrarInstitucion;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class InstitucionService {
    @Autowired
    private InstitucionNeo4jRepository institucionNeo4jRepository;
    @Autowired
    private InstitucionMONGORepository institucionMongoRepository;
    @Autowired
    private MateriaNeo4jRepository materiaNeo4jRepository;


    public Institucion crearInstitucion(RequestRegistrarInstitucion requestRegistrarInstitucion) {
            Institucion institucion = new Institucion();
            institucion.setId(requestRegistrarInstitucion.getPadron());
            institucion.setPais(requestRegistrarInstitucion.getPais());
            institucion.setProvincia(requestRegistrarInstitucion.getProvincia());
            institucion.setNivelEducativo(requestRegistrarInstitucion.getNivelEducativo());
            institucion.setCurriculum(requestRegistrarInstitucion.getCurriculum() == null
                    ? new ArrayList<>()
                    : requestRegistrarInstitucion.getCurriculum());
            institucion.setNombre(requestRegistrarInstitucion.getNombre());

            Institucion guardadaNeo4j = institucionNeo4jRepository.save(institucion);
            institucionMongoRepository.save(institucion);
            return guardadaNeo4j;
    }

    public Institucion obtenerPorId(String id) {
            Optional<Institucion> institucion = institucionNeo4jRepository.findById(id);
            return institucion.orElse(null);
    }

    public java.util.List<Institucion.Curso> obtenerCurriculum(String id) {
            Optional<Institucion> institucion = institucionNeo4jRepository.findById(id);
            if (institucion.isEmpty()) {
                return null;
            }
            if (institucion.get().getCurriculum() == null) {
                return new ArrayList<>();
            }
            return institucion.get().getCurriculum();
    }

    public Institucion modificarInstitucion(String id, RequestModificarInstitucion requestModificarInstitucion) {
            Optional<Institucion> neo4jOpt = institucionNeo4jRepository.findById(id);
            Optional<Institucion> mongoOpt = institucionMongoRepository.findById(id);
            if (neo4jOpt.isEmpty() || mongoOpt.isEmpty()) {
                return null;
            }

            Institucion neo4j = neo4jOpt.get();
            Institucion mongo = mongoOpt.get();

            aplicarCambiosSinCurriculum(neo4j, requestModificarInstitucion);
            aplicarCambiosSinCurriculum(mongo, requestModificarInstitucion);

            institucionMongoRepository.save(mongo);
            return institucionNeo4jRepository.save(neo4j);
    }

    public boolean eliminarInstitucion(String id) {
            Optional<Institucion> neo4jOpt = institucionNeo4jRepository.findById(id);
            if (neo4jOpt.isEmpty()) {
                return false;
            }
            institucionMongoRepository.deleteById(id);
            institucionNeo4jRepository.eliminarPorId(id);
            materiaNeo4jRepository.eliminarMateriasHuerfanas();
            return true;
    }

    public Institucion crearCursoYAsignarAInstitucion(String idInstitucion, RequestCurso requestCurso) {
            Optional<Institucion> neo4jOpt = institucionNeo4jRepository.findById(idInstitucion);
            Optional<Institucion> mongoOpt = institucionMongoRepository.findById(idInstitucion);
            if (neo4jOpt.isEmpty() || mongoOpt.isEmpty()) {
                return null;
            }
            Institucion neo4j = neo4jOpt.get();
            Institucion mongo = mongoOpt.get();
            if (neo4j.getCurriculum() == null) neo4j.setCurriculum(new ArrayList<>());
            if (mongo.getCurriculum() == null) mongo.setCurriculum(new ArrayList<>());

            Institucion.Curso curso = new Institucion.Curso(requestCurso.getId(), requestCurso.getNombre());
            upsertCurso(neo4j.getCurriculum(), curso);
            upsertCurso(mongo.getCurriculum(), curso);

            institucionMongoRepository.save(mongo);
            return institucionNeo4jRepository.save(neo4j);
    }

    public Institucion modificarCurso(String idInstitucion, String idCurso, RequestModificarCurso requestModificarCurso) {
            Optional<Institucion> neo4jOpt = institucionNeo4jRepository.findById(idInstitucion);
            Optional<Institucion> mongoOpt = institucionMongoRepository.findById(idInstitucion);
            if (neo4jOpt.isEmpty() || mongoOpt.isEmpty()) {
                return null;
            }
            Institucion neo4j = neo4jOpt.get();
            Institucion mongo = mongoOpt.get();
            if (neo4j.getCurriculum() == null) neo4j.setCurriculum(new ArrayList<>());
            if (mongo.getCurriculum() == null) mongo.setCurriculum(new ArrayList<>());
            boolean okNeo4j = actualizarCurso(neo4j.getCurriculum(), idCurso, requestModificarCurso);
            boolean okMongo = actualizarCurso(mongo.getCurriculum(), idCurso, requestModificarCurso);
            if (!okNeo4j && !okMongo) {
                return null;
            }
            institucionMongoRepository.save(mongo);
            return institucionNeo4jRepository.save(neo4j);
    }

    public boolean eliminarCursoDeInstitucion(String idInstitucion, String idCurso) {
            Optional<Institucion> neo4jOpt = institucionNeo4jRepository.findById(idInstitucion);
            Optional<Institucion> mongoOpt = institucionMongoRepository.findById(idInstitucion);
            if (neo4jOpt.isEmpty() || mongoOpt.isEmpty()) {
                return false;
            }
            Institucion neo4j = neo4jOpt.get();
            Institucion mongo = mongoOpt.get();
            if (neo4j.getCurriculum() == null) neo4j.setCurriculum(new ArrayList<>());
            if (mongo.getCurriculum() == null) mongo.setCurriculum(new ArrayList<>());
            boolean removedNeo4j = removerCurso(neo4j.getCurriculum(), idCurso);
            boolean removedMongo = removerCurso(mongo.getCurriculum(), idCurso);
            if (!removedNeo4j && !removedMongo) {
                return false;
            }
            institucionMongoRepository.save(mongo);
            institucionNeo4jRepository.save(neo4j);
            institucionNeo4jRepository.eliminarRelacionInstitucionCurso(idInstitucion, idCurso);
            institucionNeo4jRepository.eliminarCursosHuerfanos();
            return true;
    }

    private void aplicarCambiosSinCurriculum(Institucion institucion, RequestModificarInstitucion request) {
            if (request.getNombre() != null) {
                institucion.setNombre(request.getNombre());
            }
            if (request.getPais() != null) {
                institucion.setPais(request.getPais());
            }
            if (request.getProvincia() != null) {
                institucion.setProvincia(request.getProvincia());
            }
            if (request.getNivelEducativo() != null) {
                institucion.setNivelEducativo(request.getNivelEducativo());
            }
    }

    private void upsertCurso(java.util.List<Institucion.Curso> cursos, Institucion.Curso nuevo) {
            for (int i = 0; i < cursos.size(); i++) {
                Institucion.Curso actual = cursos.get(i);
                if (Objects.equals(actual.getId(), nuevo.getId())) {
                    cursos.set(i, nuevo);
                    return;
                }
            }
            cursos.add(nuevo);
    }

    private boolean actualizarCurso(java.util.List<Institucion.Curso> cursos, String idCurso, RequestModificarCurso request) {
            if (cursos == null) {
                return false;
            }
            for (Institucion.Curso curso : cursos) {
                if (Objects.equals(curso.getId(), idCurso)) {
                    if (request.getNombre() != null) {
                        curso.setNombre(request.getNombre());
                    }
                    return true;
                }
            }
            return false;
    }

    private boolean removerCurso(java.util.List<Institucion.Curso> cursos, String idCurso) {
            if (cursos == null) {
                return false;
            }
            return cursos.removeIf(c -> Objects.equals(c.getId(), idCurso));
    }
}
