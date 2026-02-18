package com.example.demo.service;

import com.example.demo.model.Institucion;
import com.example.demo.model.Materia;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {
    @Autowired
    private InstitucionMONGORepository institucionMONGORepository;

    @Autowired
    private Neo4jClient neo4jClient;

    public Materia registrarMateria(String institucionId, Materia materia) {
        if (materia.getId() == null || materia.getId().isBlank()) {
            throw new IllegalArgumentException("El id de la materia es obligatorio.");
        }

        Institucion institucion = obtenerInstitucion(institucionId);
        List<Institucion.MateriaInstitucion> curriculum = institucion.getCurriculum();
        if (curriculum == null) {
            curriculum = new ArrayList<>();
            institucion.setCurriculum(curriculum);
        }

        Institucion.MateriaInstitucion materiaCurriculum = toMateriaInstitucion(materia);
        curriculum.removeIf(m -> m != null && materiaCurriculum.getId().equals(m.getId()));
        curriculum.add(materiaCurriculum);
        institucionMONGORepository.save(institucion);

        Materia respuesta = toMateriaModel(materiaCurriculum);
        sincronizarMateriaEnNeo4j(institucion, respuesta);
        return respuesta;
    }

    public List<Materia> registrarMaterias(String institucionId, List<Materia> materias) {
        List<Materia> guardadas = new ArrayList<>();
        if (materias == null) {
            return guardadas;
        }
        for (Materia materia : materias) {
            guardadas.add(registrarMateria(institucionId, materia));
        }
        return guardadas;
    }

    public Materia actualizarMateria(String institucionId, String id, Materia materia) {
        Institucion institucion = obtenerInstitucion(institucionId);
        List<Institucion.MateriaInstitucion> curriculum = institucion.getCurriculum();
        if (curriculum == null) {
            throw new IllegalArgumentException("La institucion no tiene curriculum cargado: " + institucionId);
        }

        Optional<Institucion.MateriaInstitucion> existente = curriculum.stream()
                .filter(m -> m != null && id.equals(m.getId()))
                .findFirst();
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No existe materia con id: " + id);
        }

        materia.setId(id);
        Institucion.MateriaInstitucion materiaCurriculum = toMateriaInstitucion(materia);
        curriculum.removeIf(m -> m != null && id.equals(m.getId()));
        curriculum.add(materiaCurriculum);
        institucionMONGORepository.save(institucion);

        Materia respuesta = toMateriaModel(materiaCurriculum);
        sincronizarMateriaEnNeo4j(institucion, respuesta);
        return respuesta;
    }

    public Materia obtenerPorId(String institucionId, String id) {
        Institucion institucion = obtenerInstitucion(institucionId);
        List<Institucion.MateriaInstitucion> curriculum = institucion.getCurriculum();
        if (curriculum == null) {
            throw new IllegalArgumentException("La institucion no tiene curriculum cargado: " + institucionId);
        }
        Institucion.MateriaInstitucion encontrada = curriculum.stream()
                .filter(m -> m != null && id.equals(m.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe materia con id: " + id + " en la institucion: " + institucionId));
        return toMateriaModel(encontrada);
    }

    public void eliminarMateria(String institucionId, String id) {
        Institucion institucion = obtenerInstitucion(institucionId);
        List<Institucion.MateriaInstitucion> curriculum = institucion.getCurriculum();
        if (curriculum == null || curriculum.isEmpty()) {
            throw new IllegalArgumentException("La institucion no tiene curriculum cargado: " + institucionId);
        }

        boolean removida = curriculum.removeIf(m -> m != null && id.equals(m.getId()));
        if (!removida) {
            throw new IllegalArgumentException("No existe materia con id: " + id + " en la institucion: " + institucionId);
        }
        institucionMONGORepository.save(institucion);

        neo4jClient.query("""
                MATCH (i:Institucion {id: $institucionId})-[r:ENSENA]->(m:Materia {id: $id})
                DELETE r
                """)
                .bind(institucionId).to("institucionId")
                .bind(id).to("id")
                .run();

        neo4jClient.query("""
                MATCH (m:Materia {id: $id})
                WHERE NOT (()-[:ENSENA]->(m))
                  AND NOT ((:Estudiante)-[:CURSO]->(m))
                DETACH DELETE m
                """)
                .bind(id).to("id")
                .run();
    }

    public void eliminarMaterias(String institucionId, List<String> ids) {
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            if (id != null && !id.isBlank()) {
                eliminarMateria(institucionId, id);
            }
        }
    }

    private Institucion obtenerInstitucion(String institucionId) {
        return institucionMONGORepository.findById(institucionId)
                .orElseThrow(() -> new IllegalArgumentException("No existe institucion con id: " + institucionId));
    }

    private Institucion.MateriaInstitucion toMateriaInstitucion(Materia materia) {
        Institucion.MateriaInstitucion materiaInstitucion = new Institucion.MateriaInstitucion();
        materiaInstitucion.setId(materia.getId());
        materiaInstitucion.setNombre(materia.getNombre());
        materiaInstitucion.setAnioCarrera(materia.getAnioCarrera());
        return materiaInstitucion;
    }

    private Materia toMateriaModel(Institucion.MateriaInstitucion materiaInstitucion) {
        Materia materia = new Materia();
        materia.setId(materiaInstitucion.getId());
        materia.setNombre(materiaInstitucion.getNombre());
        materia.setAnioCarrera(materiaInstitucion.getAnioCarrera());
        return materia;
    }

    private void sincronizarMateriaEnNeo4j(Institucion institucion, Materia guardada) {
        neo4jClient.query("""
                MERGE (i:Institucion {id: $institucionId})
                SET i.nombre = coalesce($institucionNombre, i.nombre),
                    i.name = coalesce($institucionNombre, i.name, i.id)
                MERGE (m:Materia {id: $id})
                SET m.nombre = $nombre,
                    m.name = $id,
                    m.anioCarrera = $anioCarrera
                MERGE (i)-[:ENSENA]->(m)
                """)
                .bind(institucion.getId()).to("institucionId")
                .bind(institucion.getNombre()).to("institucionNombre")
                .bind(guardada.getId()).to("id")
                .bind(guardada.getNombre()).to("nombre")
                .bind(guardada.getAnioCarrera() != null ? guardada.getAnioCarrera() : 0).to("anioCarrera")
                .run();
    }
}
