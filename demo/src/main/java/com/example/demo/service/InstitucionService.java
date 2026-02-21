package com.example.demo.service;
import com.example.demo.model.Institucion;
import com.example.demo.model.InstitucionOpcion;
import com.example.demo.model.RequestRegistrarInstitucion;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstitucionService {
    @Autowired
    private InstitucionNeo4jRepository institucionRepository;

    // Crea una institucion validando padron obligatorio.
    public Institucion registrarInstitucion(RequestRegistrarInstitucion requestRegistrarInstitucion) {
            if (requestRegistrarInstitucion.getPadron() == null || requestRegistrarInstitucion.getPadron().isBlank()) {
                throw new IllegalArgumentException("padron es obligatorio");
            }
            Institucion i = new Institucion();
            i.setId(requestRegistrarInstitucion.getPadron().trim());
            i.setPais(requestRegistrarInstitucion.getPais());
            i.setProvincia(requestRegistrarInstitucion.getProvincia());
            i.setNivelEducativo(requestRegistrarInstitucion.getNivelEducativo());
            i.setNombre(requestRegistrarInstitucion.getNombre());
            Institucion guardada = institucionRepository.save(i);
            System.out.println("El ID generado es: " + guardada.getId());
            return guardada;
        }

    // Lista todas las instituciones registradas.
    public List<Institucion> listarInstituciones() {
        return institucionRepository.findAll();
    }

    // Busca una institucion por su ID.
    public Optional<Institucion> obtenerPorId(String id) {
        return institucionRepository.findById(id);
    }

    // Devuelve opciones resumidas de instituciones para el frontend.
    public List<InstitucionOpcion> listarOpcionesInstituciones() {
        return institucionRepository.listarOpciones();
    }
}

