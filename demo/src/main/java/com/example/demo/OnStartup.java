package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Component
public class OnStartup implements ApplicationListener<ApplicationReadyEvent> {

    private Random random = new Random();

    @Autowired
    private EstudianteNeo4jRepository estudiantesRepo;

    @Autowired
    private InstitucionNeo4jRepository institucionRepo;

    @Autowired
    private MateriaNeo4jRepository materiaRepo;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        materiaRepo.deleteAll();
        institucionRepo.deleteAll();
        estudiantesRepo.deleteAll();

        // INSTITUCIONES
        Institucion uade = insertarInstitucion("UADE");
        Institucion unlam = insertarInstitucion("UNLAM");

        // MATERIAS
        Materia matematica = insertarMateria("MATEMATICA");
        Materia literatura = insertarMateria("LITERATURA");
        Materia biologia = insertarMateria("BIOLOGIA");
        Materia gimnasia = insertarMateria("GIMNASIA");

        // ESTUDIANTES
        Estudiante daniela = insertartEstudiante("Daniela");
        Estudiante alejandro = insertartEstudiante("Alejandro");

        // ESTUDIANTE CURSO EN
        estudianteCursoEn(daniela, uade, "2023-2026");
        estudianteCursoEn(alejandro, unlam, "2007-2015");

        // ESTUDIANTE CURSO MATERIA
        estudianteCurso(daniela, matematica, 9.0);
        estudianteCurso(daniela, literatura, 8.5);
        estudianteCurso(daniela, biologia, 10.0);
        estudianteCurso(daniela, gimnasia, 5.0);

        estudianteCurso(alejandro, matematica, 7.0);
        estudianteCurso(alejandro, literatura, 4.0);
        estudianteCurso(alejandro, biologia, 6.0);
        estudianteCurso(alejandro, gimnasia, 10.0);

        estudiantesRepo.save(daniela);
        estudiantesRepo.save(alejandro);
    }

    private void estudianteCurso(Estudiante estudiante, Materia materia, double promedio) {
        CursoMateria relacion = new CursoMateria();
        relacion.setMateria(materia);
        relacion.setPromedio(promedio);
        estudiante.curso(relacion);
    }

    private void estudianteCursoEn(Estudiante estudiante, Institucion institucion, String periodo) {
        EstudioEn relacion = new EstudioEn(institucion, periodo, "");
        estudiante.getHistorialAcademico().add(relacion);
    }

    private Institucion insertarInstitucion(String nombre) {
        Institucion institucion = new Institucion();
        institucion.setNombre(nombre);
        institucionRepo.save(institucion);
        return institucion;
    }

    private Estudiante insertartEstudiante(String nombre) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre);
        estudiantesRepo.save(estudiante);
        return estudiante;
    }

    private Materia insertarMateria(String nombre) {
        Materia materia = new Materia();
        materia.setNombre(nombre);
        materiaRepo.save(materia);
        return materia;
    }
}
