package com.example.demo.service;
import com.example.demo.model.Estudiante;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteMONGORepository estudianteRepository;
    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;

    public Estudiante registrarEstudiante(RequestRegistrarEstudiante requestRegistrarEstudiante) {
        Estudiante e = new Estudiante();
        e.setId(UUID.randomUUID());
        e.setInstitucionActual(requestRegistrarEstudiante.getInstitucionActual());
        e.setNombre(requestRegistrarEstudiante.getNombre());
        e.setPaisOrigen(requestRegistrarEstudiante.getPaisOrigen());
        e.setEmail(requestRegistrarEstudiante.getEmail());
        e.setHistorial(requestRegistrarEstudiante.getMetadatos());
        return estudianteRepository.save(e);
    }


    public void asociarInstitucionPrevia(String estudianteId,String institucionId) {
        estudianteNeo4jRepository.registrarHistorial(estudianteId, institucionId);
    }

    public List<Map<String, Object>> consultarHistorial(String id) {
        return estudianteNeo4jRepository.obtenerHistorialAcademico(id);
    }
}
