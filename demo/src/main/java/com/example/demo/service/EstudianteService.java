package com.example.demo.service;
import com.example.demo.model.Estudiante;
import com.example.demo.model.Institucion;
import com.example.demo.model.RequestModificarEstudiante;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;
    @Autowired
    private EstudianteMONGORepository estudianteMongoRepository;
    @Autowired
    private InstitucionNeo4jRepository institucionNeo4jRepository;


    public Estudiante crearEstudiante(RequestRegistrarEstudiante requestRegistrarEstudiante) {
        validarAlta(requestRegistrarEstudiante);
        Estudiante e = new Estudiante();
        e.setId(requestRegistrarEstudiante.getIdNacional().trim());
        e.setNombre(requestRegistrarEstudiante.getNombre());
        e.setPaisOrigen(requestRegistrarEstudiante.getPaisOrigen());
        e.setEmail(requestRegistrarEstudiante.getEmail());
        Institucion institucionActual = obtenerInstitucion(requestRegistrarEstudiante.getInstitucionActual());
        e.setInstitucionActual(institucionActual);
        Estudiante guardadoNeo4j = estudianteNeo4jRepository.save(e);
        if (institucionActual != null) {
            estudianteNeo4jRepository.registrarDondeEstudio(e.getId(), institucionActual.getId(), "ACTUAL");
        }
        estudianteMongoRepository.save(e);
        return guardadoNeo4j;
    }

    public Estudiante obtenerPorId(String id) {
        Optional<Estudiante> estudiante = estudianteNeo4jRepository.findById(id);
        return estudiante.orElse(null);
    }

    public Estudiante modificarEstudiante(String id, RequestModificarEstudiante requestModificarEstudiante) {
        Optional<Estudiante> neo4jOpt = estudianteNeo4jRepository.findById(id);
        Optional<Estudiante> mongoOpt = estudianteMongoRepository.findById(id);
        if (neo4jOpt.isEmpty() || mongoOpt.isEmpty()) {
            return null;
        }

        Estudiante neo4j = neo4jOpt.get();
        Estudiante mongo = mongoOpt.get();

        aplicarCambiosSinMateriasNiHistorial(neo4j, requestModificarEstudiante);
        aplicarCambiosSinMateriasNiHistorial(mongo, requestModificarEstudiante);

        estudianteMongoRepository.save(mongo);
        return estudianteNeo4jRepository.save(neo4j);
    }

    public boolean eliminarEstudiante(String id) {
        Optional<Estudiante> neo4jOpt = estudianteNeo4jRepository.findById(id);
        if (neo4jOpt.isEmpty()) {
            return false;
        }
        estudianteMongoRepository.deleteById(id);
        estudianteNeo4jRepository.eliminarPorId(id);
        return true;
    }

    private void aplicarCambiosSinMateriasNiHistorial(Estudiante estudiante, RequestModificarEstudiante request) {
        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            estudiante.setNombre(request.getNombre().trim());
        }
        if (request.getPaisOrigen() != null) {
            estudiante.setPaisOrigen(request.getPaisOrigen());
        }
        if (request.getInstitucionActual() != null) {
            Institucion institucionActual = obtenerInstitucion(request.getInstitucionActual());
            estudiante.setInstitucionActual(institucionActual);
            if (institucionActual != null) {
                estudianteNeo4jRepository.registrarDondeEstudio(estudiante.getId(), institucionActual.getId(), "ACTUAL");
            }
        }
        if (request.getEmail() != null) {
            estudiante.setEmail(request.getEmail());
        }

    }

    private void validarAlta(RequestRegistrarEstudiante request) {
        if (request.getIdNacional() == null || request.getIdNacional().isBlank()) {
            throw new IllegalArgumentException("El idNacional es obligatorio.");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        request.setNombre(request.getNombre().trim());
    }

    private Institucion obtenerInstitucion(String idInstitucion) {
        if (idInstitucion == null || idInstitucion.isBlank()) {
            return null;
        }
        return institucionNeo4jRepository.findById(idInstitucion).orElse(null);
    }
}
