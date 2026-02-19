package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class OnStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EstudianteNeo4jRepository estudiantesRepo;

    @Autowired
    private InstitucionNeo4jRepository institucionRepo;
    @Autowired
    private InstitucionMONGORepository institucionMongoRepo;
    @Autowired
    private EstudianteMONGORepository estudianteMongoRepo;

    @Autowired
    private MateriaNeo4jRepository materiaRepo;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        materiaRepo.deleteAll();
        institucionRepo.deleteAll();
        institucionMongoRepo.deleteAll();
        estudiantesRepo.deleteAll();
        estudianteMongoRepo.deleteAll();

        // MATERIAS
        Materia matematica = insertarMateria("M001", "MATEMATICA");
        Materia literatura = insertarMateria("M002", "LITERATURA");
        Materia biologia = insertarMateria("M003", "BIOLOGIA");
        Materia arte = insertarMateria("M004", "ARTE");
        Materia futbol = insertarMateria("M005", "FUTBOL");
        Materia gimnasia = insertarMateria("M006", "GIMNASIA");
        Materia fisica = insertarMateria("M007", "FISICA");
        Materia quimica = insertarMateria("M008", "QUIMICA");
        Materia carpinteria = insertarMateria("M009", "CARPINTERIA");
        Materia informatica = insertarMateria("M010", "INFORMATICA");

        // INSTITUCIONES
        Institucion uade = insertarInstitucion(
                "PADRON-007",
                "Colegio Nuevo Milenio",
                "Sudafrica",
                "Gauteng",
                "Secundaria",
                Arrays.asList(
                        materiaCurricular(matematica),
                        materiaCurricular(literatura),
                        materiaCurricular(biologia),
                        materiaCurricular(gimnasia),
                        materiaCurricular(informatica)));
        Institucion unlam = insertarInstitucion(
                "PADRON-008",
                "Colegio 3 de febrero",
                "Sudafrica",
                "Western Cape",
                "Secundaria",
                Arrays.asList(
                        materiaCurricular(matematica),
                        materiaCurricular(arte),
                        materiaCurricular(futbol),
                        materiaCurricular(fisica),
                        materiaCurricular(quimica),
                        materiaCurricular(carpinteria)));

        // ESTUDIANTES
        Estudiante daniela = insertartEstudiante("35266014", "Daniela", "Argentina");
        Estudiante alejandro = insertartEstudiante("34111222", "Alejandro", "Argentina");
        Estudiante martin = insertartEstudiante("33666777", "Martin", "Argentina");
        Estudiante tomas = insertartEstudiante("32999888", "Tomas", "Argentina");
        Estudiante gabriela = insertartEstudiante("31888555", "Gabriela", "Argentina");
        Estudiante juana = insertartEstudiante("30777444", "Juana", "Argentina");


        // ESTUDIANTE CURSO EN
        estudianteCursoEn(daniela, uade, "2023-2026");
        estudianteCursoEn(alejandro, unlam, "2007-2015");
        estudianteCursoEn(martin, uade, "2023-2026");
        estudianteCursoEn(tomas, unlam, "2007-2015");
        estudianteCursoEn(gabriela, uade, "2023-2026");
        estudianteCursoEn(juana, unlam, "2007-2015");

        // ESTUDIANTE CURSO MATERIA
        estudianteCurso(daniela, matematica, 9.0);
        estudianteCurso(daniela, literatura, 8.5);
        estudianteCurso(daniela, biologia, 10.0);
        estudianteCurso(daniela, gimnasia, 5.0);
        estudianteCurso(daniela, arte, 9.0);
        estudianteCurso(daniela, futbol, 8.5);
        estudianteCurso(daniela, fisica, 10.0);
        estudianteCurso(daniela, quimica, 5.0);
        estudianteCurso(daniela, carpinteria, 9.0);
        estudianteCurso(daniela, informatica, 8.5);



        estudianteCurso(alejandro, matematica, 7.0);
        estudianteCurso(alejandro, literatura, 4.0);
        estudianteCurso(alejandro, biologia, 6.0);
        estudianteCurso(alejandro, gimnasia, 10.0);
        estudianteCurso(alejandro, arte, 9.0);
        estudianteCurso(alejandro, futbol, 8.5);
        estudianteCurso(alejandro, fisica, 10.0);
        estudianteCurso(alejandro, quimica, 5.0);
        estudianteCurso(alejandro, carpinteria, 9.0);
        estudianteCurso(alejandro, informatica, 8.5);

        estudiantesRepo.save(daniela);
        estudiantesRepo.save(alejandro);
        estudiantesRepo.save(martin);
        estudiantesRepo.save(tomas);
        estudiantesRepo.save(gabriela);
        estudiantesRepo.save(juana);
    }

    private void estudianteCurso(Estudiante estudiante, Materia materia, double promedio) {
        CursoMateria relacion = new CursoMateria();
        relacion.setMateria(materia);
        relacion.setPromedio(promedio);
        estudiante.curso(relacion);
    }

    private void estudianteCursoEn(Estudiante estudiante, Institucion institucion, String periodo) {
        EstudioEn relacion = new EstudioEn(institucion, new java.util.ArrayList<>());
        estudiante.getHistorialAcademico().add(relacion);
    }

    private Institucion insertarInstitucion(
            String padron,
            String nombre,
            String pais,
            String provincia,
            String nivelEducativo,
            List<Institucion.Curso> curriculum) {
        Institucion institucion = new Institucion();
        institucion.setId(padron);
        institucion.setNombre(nombre);
        institucion.setPais(pais);
        institucion.setProvincia(provincia);
        institucion.setNivelEducativo(nivelEducativo);
        institucion.setCurriculum(curriculum);
        institucionRepo.save(institucion);
        institucionMongoRepo.save(institucion);
        return institucion;
    }

    private Institucion.Curso materiaCurricular(Materia materia) {
        return new Institucion.Curso(materia.getId(), materia.getNombre());
    }

    private Estudiante insertartEstudiante(String idNacional, String nombre, String pais) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(idNacional);
        estudiante.setNombre(nombre);
        estudiante.setPaisOrigen(pais);
        estudiantesRepo.save(estudiante);
        estudianteMongoRepo.save(estudiante);
        return estudiante;
    }

    private Materia insertarMateria(String codigo, String nombre) {
        Materia materia = new Materia();
        materia.setId(codigo);
        materia.setNombre(nombre);
        materiaRepo.save(materia);
        return materia;
    }
}
