package com.example.demo.service;
import com.example.demo.model.Estudiante;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EstudianteService {
    @Autowired
    private EstudianteNeo4jRepository estudianteNeo4jRepository;
    @Autowired
    private InstitucionNeo4jRepository instRepo;

    public void procesarHistorial(Estudiante estudiante) {
//        Estudiante e = new Estudiante(estudiante.getId(), estudiante.getNombre());
//
//        Map<String, Object> historial = (Map<String, Object>) estudiante.getHistorial().get("historial");
//
//        if (historial.containsKey("institucion1") && historial.containsKey("primaria")) {
//            String nombreInst = (String) historial.get("institucion1");
//            String periodo = (String) historial.get("primaria");
//
//            Institucion inst = instRepo.findByNombre(nombreInst);
//
//            if (inst != null) {
//                estudiante.agregarEstudio(inst, periodo, "primaria");
//            }
//        }
//
//        neo4jRepo.save(estudiante);
    }

    public Estudiante registrarEstudiante(RequestRegistrarEstudiante requestRegistrarEstudiante) {
        Estudiante e = new Estudiante();
        e.setId(UUID.randomUUID());
        e.setInstitucionActual(requestRegistrarEstudiante.getInstitucionActual());
        e.setNombre(requestRegistrarEstudiante.getNombre());
        e.setPaisOrigen(requestRegistrarEstudiante.getPaisOrigen());
        e.setEmail(requestRegistrarEstudiante.getEmail());
        //e.setHistorial(requestRegistrarEstudiante.getHistorial());
        return estudianteNeo4jRepository.save(e);
    }

    public List<Map<String, Object>> consultarHistorial(String id) {
        return estudianteNeo4jRepository.obtenerHistorialAcademico(UUID.fromString(id));
    }
}