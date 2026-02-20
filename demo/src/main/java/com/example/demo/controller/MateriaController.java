package com.example.demo.controller;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestRegistrarMateria;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import com.example.demo.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registrar")
    public ResponseEntity<Materia> registrar(@RequestBody
                                                  RequestRegistrarMateria requestRegistrarMateria) {

        Materia nueva = materiaService.registrarMateria(
                requestRegistrarMateria);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/instituciones/{idInstitucion}/opciones")
    public List<Map<String, Object>> listarMateriasPorInstitucion(@PathVariable("idInstitucion") String idInstitucion) {
        return materiaRepository.listarMateriasPorInstitucion(idInstitucion);
    }
}
