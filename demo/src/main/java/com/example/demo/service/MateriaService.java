package com.example.demo.service;

import com.example.demo.model.Materia;
import com.example.demo.model.RequestRegistrarEquivalenciaMateria;
import com.example.demo.model.RequestRegistrarMateria;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MateriaService {

    @Autowired
    private MateriaNeo4jRepository materiaRepository;
    @Autowired
    private Neo4jClient neo4jClient;

    // Registra una materia base en Neo4j usando codigo y nombre.
    public Materia registrarMateria(RequestRegistrarMateria requestRegistrarMateria) {
        if (requestRegistrarMateria.getCodigo() == null || requestRegistrarMateria.getCodigo().isBlank()) {
            throw new IllegalArgumentException("codigo es obligatorio");
        }
        Materia m = new Materia();
        m.setId(requestRegistrarMateria.getCodigo().trim());
        m.setNombre(requestRegistrarMateria.getNombre());

        return materiaRepository.save(m);
    }

    // Crea equivalencia entre materia origen y destino validando universidades.
    public void registrarEquivalencia(RequestRegistrarEquivalenciaMateria request) {
        validarRequestEquivalencia(request);

        boolean origenValido = materiaRepository.existeMateriaUniversitaria(
                request.getInstitucionOrigenId(), request.getMateriaOrigenId());
        if (!origenValido) {
            throw new IllegalArgumentException("La materia origen no pertenece a una universidad valida");
        }

        boolean destinoValido = materiaRepository.existeMateriaUniversitaria(
                request.getInstitucionDestinoId(), request.getMateriaDestinoId());
        if (!destinoValido) {
            throw new IllegalArgumentException("La materia destino no pertenece a una universidad valida");
        }

        materiaRepository.registrarEquivalencia(
                request.getInstitucionOrigenId(),
                request.getMateriaOrigenId(),
                request.getInstitucionDestinoId(),
                request.getMateriaDestinoId());
    }

    // Lista equivalencias de una materia con datos de la institucion destino.
    public List<Map<String, Object>> listarEquivalencias(String idMateria) {
        if (idMateria == null || idMateria.isBlank()) {
            throw new IllegalArgumentException("idMateria es obligatorio");
        }
        String cypher = """
                MATCH (m1:Materia {id: $idMateria})-[r:EQUIVALENCIA]->(m2:Materia)
                OPTIONAL MATCH (i:Institucion)-[:OFRECE]->(m2)
                RETURN m2.id AS idMateriaEquivalente,
                       m2.nombre AS nombreMateriaEquivalente,
                       i.id AS idInstitucionDestino,
                       i.nombre AS nombreInstitucionDestino,
                       coalesce(r.activa, true) AS activa
                ORDER BY nombreInstitucionDestino, nombreMateriaEquivalente
                """;

        return new ArrayList<>(neo4jClient.query(cypher)
                .bind(idMateria.trim()).to("idMateria")
                .fetch()
                .all());
    }

    // Valida campos requeridos para registrar equivalencias.
    private void validarRequestEquivalencia(RequestRegistrarEquivalenciaMateria request) {
        if (request == null) {
            throw new IllegalArgumentException("request es obligatorio");
        }
        if (request.getInstitucionOrigenId() == null || request.getInstitucionOrigenId().isBlank()) {
            throw new IllegalArgumentException("institucionOrigenId es obligatorio");
        }
        if (request.getMateriaOrigenId() == null || request.getMateriaOrigenId().isBlank()) {
            throw new IllegalArgumentException("materiaOrigenId es obligatorio");
        }
        if (request.getInstitucionDestinoId() == null || request.getInstitucionDestinoId().isBlank()) {
            throw new IllegalArgumentException("institucionDestinoId es obligatorio");
        }
        if (request.getMateriaDestinoId() == null || request.getMateriaDestinoId().isBlank()) {
            throw new IllegalArgumentException("materiaDestinoId es obligatorio");
        }
        if (request.getMateriaOrigenId().trim().equals(request.getMateriaDestinoId().trim())) {
            throw new IllegalArgumentException("No se puede crear equivalencia entre la misma materia");
        }
    }
}
