package com.example.demo;

import com.example.demo.model.CursoMateria;
import com.example.demo.model.Estudiante;
import com.example.demo.model.EstudioEn;
import com.example.demo.model.Institucion;
import com.example.demo.model.LegislacionConversion;
import com.example.demo.model.Materia;
import com.example.demo.repository.mongo.LegislacionConversionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class OnStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EstudianteNeo4jRepository estudiantesRepo;

    @Autowired
    private InstitucionNeo4jRepository institucionRepo;

    @Autowired
    private MateriaNeo4jRepository materiaRepo;

    @Autowired
    private LegislacionConversionMONGORepository legislacionRepo;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        materiaRepo.deleteAll();
        institucionRepo.deleteAll();
        estudiantesRepo.deleteAll();

        seedLegislacionConversion();

        // MATERIAS
        Materia matematica = insertarMateria("CUR-MAT", "MATEMATICA");
        Materia literatura = insertarMateria("CUR-LIT", "LITERATURA");
        Materia biologia = insertarMateria("CUR-BIO", "BIOLOGIA");
        Materia arte = insertarMateria("CUR-ART", "ARTE");
        Materia futbol = insertarMateria("CUR-FUT", "FUTBOL");
        Materia gimnasia = insertarMateria("CUR-GIM", "GIMNASIA");
        Materia fisica = insertarMateria("CUR-FIS", "FISICA");
        Materia quimica = insertarMateria("CUR-QUI", "QUIMICA");
        Materia carpinteria = insertarMateria("CUR-CAR", "CARPINTERIA");
        Materia informatica = insertarMateria("CUR-INF", "INFORMATICA");

        // INSTITUCIONES CON CURRICULUM
        Institucion uade = insertarInstitucion(
                "PADRON-001",
                "Colegio Nuevo Milenio",
                matematica, literatura, biologia, arte, informatica, fisica);
        Institucion unlam = insertarInstitucion(
                "PADRON-002",
                "Colegio 3 de febrero",
                matematica, quimica, futbol, gimnasia, carpinteria, informatica);

        // ESTUDIANTES
        Estudiante daniela = insertartEstudiante("35111222", "Daniela", "Argentina");
        Estudiante alejandro = insertartEstudiante("34111222", "Alejandro", "Argentina");
        Estudiante martin = insertartEstudiante("33111222", "Martin", "Argentina");
        Estudiante tomas = insertartEstudiante("32111222", "Tomas", "Argentina");
        Estudiante gabriela = insertartEstudiante("31111222", "Gabriela", "Argentina");
        Estudiante juana = insertartEstudiante("30111222", "Juana", "Argentina");


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

    private void seedLegislacionConversion() {
        legislacionRepo.deleteAll();

        LegislacionConversion doc = new LegislacionConversion();
        doc.setId("SA_EQUIVALENCIAS_V2");
        doc.setVersion(2);
        doc.setVigenciaDesde(LocalDate.of(2016, 1, 1));
        doc.setVigenciaHasta(null);
        doc.setUkNotas(mapaUk());
        doc.setArgentinaNotas(mapaArgentina());
        doc.setUsaSemester(mapaUsaSemester2016());
        doc.setUsaGpa(listaUsaGpa());
        doc.setAlemaniaBase(5.0);
        doc.setAlemaniaFactor(25.0);

        legislacionRepo.save(doc);
    }

    private Map<String, Double> mapaUk() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("A*", 100.0);
        map.put("A", 90.0);
        map.put("B", 80.0);
        map.put("C", 70.0);
        map.put("D", 60.0);
        map.put("E", 50.0);
        map.put("F", 40.0);
        return map;
    }

    private Map<Integer, Double> mapaArgentina() {
        Map<Integer, Double> map = new LinkedHashMap<>();
        map.put(10, 100.0);
        map.put(9, 90.0);
        map.put(8, 80.0);
        map.put(7, 70.0);
        map.put(6, 60.0);
        map.put(5, 50.0);
        map.put(4, 50.0);
        return map;
    }

    private Map<String, Double> mapaUsaSemester2016() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("A", 90.0);
        map.put("B", 80.0);
        map.put("C", 70.0);
        map.put("D", 60.0);
        map.put("E", 50.0);
        map.put("F", 30.0);
        return map;
    }

    private List<LegislacionConversion.UmbralGpa> listaUsaGpa() {
        List<LegislacionConversion.UmbralGpa> lista = new ArrayList<>();
        lista.add(new LegislacionConversion.UmbralGpa(4.0, 100.0));
        lista.add(new LegislacionConversion.UmbralGpa(3.7, 90.0));
        lista.add(new LegislacionConversion.UmbralGpa(3.3, 80.0));
        lista.add(new LegislacionConversion.UmbralGpa(3.0, 70.0));
        lista.add(new LegislacionConversion.UmbralGpa(2.7, 60.0));
        lista.add(new LegislacionConversion.UmbralGpa(2.3, 50.0));
        lista.add(new LegislacionConversion.UmbralGpa(2.0, 40.0));
        lista.add(new LegislacionConversion.UmbralGpa(0.0, 30.0));
        return lista;
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

    private Institucion insertarInstitucion(String padron, String nombre, Materia... curriculum) {
        Institucion institucion = new Institucion();
        institucion.setId(padron);
        institucion.setNombre(nombre);
        List<Materia> materias = new ArrayList<>();
        if (curriculum != null) {
            for (Materia materia : curriculum) {
                materias.add(materia);
            }
        }
        institucion.setCurriculum(materias);
        institucionRepo.save(institucion);
        return institucion;
    }

    private Estudiante insertartEstudiante(String idNacional, String nombre, String pais) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(idNacional);
        estudiante.setNombre(nombre);
        estudiante.setPaisOrigen(pais);
        estudiantesRepo.save(estudiante);
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
