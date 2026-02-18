package com.example.demo.service;

import com.example.demo.model.Institucion;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstitucionService {
    @Autowired
    private InstitucionMONGORepository institucionRepository;

    @Autowired
    private Neo4jClient neo4jClient;

    public Institucion registrarInstitucion(
       Institucion institucion
    ) {
        if (institucion.getId() == null || institucion.getId().isBlank()) {
            if (institucion.getNombre() != null && !institucion.getNombre().isBlank()) {
                institucion.setId(institucion.getNombre().trim());
            } else {
                institucion.setId(UUID.randomUUID().toString());
            }
        }

        institucion.setCurriculum(normalizarCurriculum(institucion.getCurriculum()));

        Institucion guardada = institucionRepository.save(institucion);
        sincronizarInstitucionEnNeo4j(guardada);
        return guardada;
    }

    public List<Institucion> registrarInstituciones(List<Institucion> instituciones) {
        List<Institucion> guardadas = new ArrayList<>();
        if (instituciones == null) {
            return guardadas;
        }
        for (Institucion institucion : instituciones) {
            guardadas.add(registrarInstitucion(institucion));
        }
        return guardadas;
    }

    public Institucion actualizarInstitucion(String id, Institucion institucion) {
        Optional<Institucion> existente = institucionRepository.findById(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No existe institucion con id: " + id);
        }

        Institucion base = existente.get();
        Institucion actualizada = new Institucion();
        actualizada.setId(id);
        actualizada.setNombre(institucion.getNombre());
        actualizada.setPais(institucion.getPais());
        actualizada.setRegion(institucion.getRegion());
        actualizada.setProvincia(institucion.getProvincia());
        actualizada.setMetadatos(institucion.getMetadatos());
        // El curriculum se administra solo via MateriaController.
        actualizada.setCurriculum(normalizarCurriculum(base.getCurriculum()));

        actualizada = institucionRepository.save(actualizada);
        sincronizarInstitucionEnNeo4j(actualizada);
        return actualizada;
    }

    public Institucion obtenerPorId(String id) {
        return institucionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe institucion con id: " + id));
    }

    public void eliminarInstitucion(String id) {
        if (!institucionRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe institucion con id: " + id);
        }
        institucionRepository.deleteById(id);
        neo4jClient.query("""
                MATCH (i:Institucion {id: $id})
                DETACH DELETE i
                """)
                .bind(id).to("id")
                .run();

        neo4jClient.query("""
                MATCH (m:Materia)
                WHERE NOT (()-[:ENSENA]->(m))
                  AND NOT ((:Estudiante)-[:CURSO]->(m))
                DETACH DELETE m
                """)
                .run();
    }

    public void eliminarInstituciones(List<String> ids) {
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            if (id != null && !id.isBlank()) {
                eliminarInstitucion(id);
            }
        }
    }

    private void sincronizarInstitucionEnNeo4j(Institucion guardada) {
        neo4jClient.query("""
                MERGE (i:Institucion {id: $id})
                SET i.nombre = $nombre,
                    i.name = $nombre,
                    i.pais = $pais,
                    i.region = $region,
                    i.provincia = $provincia
                """)
                .bind(guardada.getId()).to("id")
                .bind(guardada.getNombre()).to("nombre")
                .bind(guardada.getPais()).to("pais")
                .bind(guardada.getRegion()).to("region")
                .bind(guardada.getProvincia()).to("provincia")
                .run();

        // Reemplaza el curriculum de la institucion en Neo4j para reflejar el estado actual en Mongo.
        neo4jClient.query("""
                MATCH (i:Institucion {id: $institucionId})-[r:ENSENA]->(:Materia)
                DELETE r
                """)
                .bind(guardada.getId()).to("institucionId")
                .run();

        if (guardada.getCurriculum() != null) {
            for (Institucion.MateriaInstitucion materia : guardada.getCurriculum()) {
                if (materia == null) {
                    continue;
                }

                if (materia.getId() == null || materia.getId().isBlank()) {
                    throw new IllegalArgumentException("Cada materia del curriculum debe incluir id.");
                }

                Integer anioCarrera = materia.getAnioCarrera();

                neo4jClient.query("""
                        MERGE (m:Materia {id: $materiaId})
                        SET m.name = $materiaId,
                            m.nombre = coalesce($materiaNombre, m.nombre),
                            m.anioCarrera = coalesce($anioCarrera, m.anioCarrera)
                        WITH m
                        MATCH (i:Institucion {id: $institucionId})
                        MERGE (i)-[:ENSENA]->(m)
                        """)
                        .bind(materia.getId()).to("materiaId")
                        .bind(materia.getNombre()).to("materiaNombre")
                        .bind(anioCarrera != null ? anioCarrera : 0).to("anioCarrera")
                        .bind(guardada.getId()).to("institucionId")
                        .run();
            }
        }

        // Limpia materias que quedaron sin relacion academica activa.
        neo4jClient.query("""
                MATCH (m:Materia)
                WHERE NOT (()-[:ENSENA]->(m))
                  AND NOT ((:Estudiante)-[:CURSO]->(m))
                DETACH DELETE m
                """)
                .run();
    }

    private List<Institucion.MateriaInstitucion> normalizarCurriculum(List<Institucion.MateriaInstitucion> curriculum) {
        List<Institucion.MateriaInstitucion> normalizado = new ArrayList<>();
        if (curriculum == null) {
            return normalizado;
        }

        for (Institucion.MateriaInstitucion materia : curriculum) {
            if (materia == null) {
                continue;
            }
            Institucion.MateriaInstitucion limpia = new Institucion.MateriaInstitucion();
            limpia.setId(materia.getId());
            limpia.setNombre(materia.getNombre());
            limpia.setAnioCarrera(materia.getAnioCarrera());
            normalizado.add(limpia);
        }
        return normalizado;
    }
}


