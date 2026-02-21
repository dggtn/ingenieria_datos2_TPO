package com.example.demo.controller;
import com.example.demo.model.Materia;
import com.example.demo.model.MateriaOpcion;
import com.example.demo.model.RequestRegistrarEquivalenciaMateria;
import com.example.demo.model.RequestRegistrarMateria;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import com.example.demo.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    @Autowired
    private MateriaNeo4jRepository materiaRepository;
    @Autowired
    private MateriaService materiaService;

    // Crea una materia base en Neo4j.
    @PostMapping("/registrar")
    public ResponseEntity<Materia> registrar(@RequestBody
                                                  RequestRegistrarMateria requestRegistrarMateria) {

        Materia nueva = materiaService.registrarMateria(
                requestRegistrarMateria);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // Registra una equivalencia entre dos materias universitarias.
    @PostMapping("/equivalencias")
    public ResponseEntity<Map<String, String>> registrarEquivalencia(@RequestBody RequestRegistrarEquivalenciaMateria request) {
        materiaService.registrarEquivalencia(request);
        Map<String, String> out = new HashMap<>();
        out.put("mensaje", "Equivalencia registrada correctamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    // Lista equivalencias de una materia origen.
    @GetMapping("/{idMateria}/equivalencias")
    public ResponseEntity<List<Map<String, Object>>> listarEquivalencias(@PathVariable("idMateria") String idMateria) {
        return ResponseEntity.ok(materiaService.listarEquivalencias(idMateria));
    }

    // Lista materias ofrecidas por una institucion.
    @GetMapping("/instituciones/{idInstitucion}/opciones")
    public List<MateriaOpcion> listarMateriasPorInstitucion(@PathVariable("idInstitucion") String idInstitucion) {
        return materiaRepository.listarMateriasPorInstitucion(idInstitucion);
    }

    // Lista todas las materias para selects del frontend.
    @GetMapping("/opciones")
    public List<Map<String, Object>> listarOpcionesMaterias() {
        List<Map<String, Object>> salida = new ArrayList<>();
        List<Materia> materias = materiaRepository.findAll();
        materias.sort(Comparator.comparing(Materia::getNombre, Comparator.nullsLast(String::compareToIgnoreCase)));
        for (Materia materia : materias) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("id", materia.getId());
            fila.put("nombre", materia.getNombre());
            salida.add(fila);
        }
        return salida;
    }
}
