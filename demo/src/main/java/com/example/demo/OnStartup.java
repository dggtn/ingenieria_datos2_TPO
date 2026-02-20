package com.example.demo;

import com.example.demo.model.CursoMateria;
import com.example.demo.model.Estudiante;
import com.example.demo.model.EstudioEn;
import com.example.demo.model.Institucion;
import com.example.demo.model.LegislacionConversion;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.model.RequestRegistrarEstudiante;
import com.example.demo.repository.cassandra.CalificacionCassandraRepository;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.mongo.EstudianteMONGORepository;
import com.example.demo.repository.mongo.LegislacionConversionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import com.example.demo.repository.neo4j.InstitucionNeo4jRepository;
import com.example.demo.repository.neo4j.MateriaNeo4jRepository;
import com.example.demo.service.CalificacionService;
import com.example.demo.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private EstudianteMONGORepository estudianteMongoRepo;
    @Autowired
    private CalificacionMONGORepository calificacionMongoRepo;
    @Autowired
    private CalificacionCassandraRepository calificacionCassandraRepo;
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private CalificacionService calificacionService;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        materiaRepo.deleteAll();
        institucionRepo.deleteAll();
        estudiantesRepo.deleteAll();
        estudianteMongoRepo.deleteAll();
        calificacionMongoRepo.deleteAll();
        calificacionCassandraRepo.deleteAll();

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
                "Argentina",
                "Buenos Aires",
                "Secundaria",
                matematica, literatura, biologia, arte, informatica, fisica);
        Institucion unlam = insertarInstitucion(
                "PADRON-002",
                "Colegio 3 de febrero",
                "USA",
                "California",
                "Secundaria",
                matematica, quimica, futbol, gimnasia, carpinteria, informatica);
        Institucion ukLondon = insertarInstitucion(
                "PADRON-UK-001",
                "London College of Sciences",
                "UK",
                "England",
                "Secundaria",
                matematica, literatura, biologia, arte, informatica, fisica);
        Institucion deBerlin = insertarInstitucion(
                "PADRON-DE-001",
                "Berlin Technische Schule",
                "Alemania",
                "Berlin",
                "Secundaria",
                matematica, quimica, fisica, informatica, carpinteria, gimnasia);
        Institucion zaCapeTown = insertarInstitucion(
                "PADRON-ZA-001",
                "Cape Town Central School",
                "Sudafrica",
                "Western Cape",
                "Secundaria",
                matematica, biologia, fisica, informatica, literatura);
        Institucion zaJohannesburg = insertarInstitucion(
                "PADRON-ZA-002",
                "Johannesburg Technical College",
                "Sudafrica",
                "Gauteng",
                "Universidad",
                quimica, fisica, informatica, carpinteria, gimnasia);
        Institucion zaPretoriaPrimary = insertarInstitucion(
                "PADRON-ZA-003",
                "Pretoria Primary Academy",
                "Sudafrica",
                "Gauteng",
                "Primaria",
                matematica, literatura, arte, biologia);
        Institucion zaDurbanHigh = insertarInstitucion(
                "PADRON-ZA-004",
                "Durban Coast High School",
                "Sudafrica",
                "KwaZulu-Natal",
                "Secundaria",
                matematica, quimica, fisica, informatica, biologia);
        Institucion zaBloemfonteinUni = insertarInstitucion(
                "PADRON-ZA-005",
                "Bloemfontein Science University",
                "Sudafrica",
                "Free State",
                "Universidad",
                quimica, fisica, informatica, matematica, literatura);
        Institucion zaPortElizabethPrimary = insertarInstitucion(
                "PADRON-ZA-006",
                "Port Elizabeth Learning Center",
                "Sudafrica",
                "Eastern Cape",
                "Primaria",
                matematica, literatura, arte, gimnasia);

        // ESTUDIANTES Y CALIFICACIONES POR API
        crearEstudianteViaApi("35111222", "Daniela", "Argentina", "PADRON-001", "daniela@mail.com");
        crearEstudianteViaApi("34111222", "Alejandro", "USA", "PADRON-002", "alejandro@mail.com");
        crearEstudianteViaApi("33111222", "Martin", "UK", "PADRON-UK-001", "martin@mail.com");
        crearEstudianteViaApi("32111222", "Tomas", "Alemania", "PADRON-DE-001", "tomas@mail.com");
        crearEstudianteViaApi("31111222", "Gabriela", "Argentina", "PADRON-001", "gabriela@mail.com");
        crearEstudianteViaApi("30111222", "Juana", "USA", "PADRON-002", "juana@mail.com");
        crearEstudianteViaApi("29111222", "Thabo Mokoena", "Sudafrica", "PADRON-ZA-001", "thabo.mokoena@mail.com");
        crearEstudianteViaApi("28111222", "Lerato Dlamini", "Sudafrica", "PADRON-ZA-002", "lerato.dlamini@mail.com");
        crearEstudianteViaApi("27111222", "Sipho Nkosi", "Sudafrica", "PADRON-ZA-003", "sipho.nkosi@mail.com");
        crearEstudianteViaApi("26111222", "Anele Khumalo", "Sudafrica", "PADRON-ZA-004", "anele.khumalo@mail.com");
        crearEstudianteViaApi("25111222", "Nandi Maseko", "Sudafrica", "PADRON-ZA-005", "nandi.maseko@mail.com");
        crearEstudianteViaApi("24111222", "Zola Mbatha", "Sudafrica", "PADRON-ZA-006", "zola.mbatha@mail.com");

        // Argentina
        registrarCalificacionArgentina("35111222", "PADRON-001", "CUR-MAT", 9, 8, 10, "2023-2024", "SECUNDARIA");
        registrarCalificacionArgentina("35111222", "PADRON-001", "CUR-LIT", 8, 9, 8, "2023-2024", "SECUNDARIA");
        registrarCalificacionArgentina("31111222", "PADRON-001", "CUR-BIO", 7, 8, 9, "2023-2024", "SECUNDARIA");
        registrarCalificacionArgentina("31111222", "PADRON-001", "CUR-FIS", 8, 7, 8, "2023-2024", "SECUNDARIA");

        // USA
        registrarCalificacionUsa("34111222", "PADRON-002", "CUR-QUI", "B", "A", "2018-2019", "SECUNDARIA");
        registrarCalificacionUsa("34111222", "PADRON-002", "CUR-INF", "A", "B", "2018-2019", "SECUNDARIA");
        registrarCalificacionUsa("30111222", "PADRON-002", "CUR-GIM", "C", "B", "2019-2020", "SECUNDARIA");
        registrarCalificacionUsa("30111222", "PADRON-002", "CUR-FUT", "B", "B", "2019-2020", "SECUNDARIA");

        // UK
        registrarCalificacionUk("33111222", "PADRON-UK-001", "CUR-ART", "A", "B", "A*", "2020-2021", "SECUNDARIA");
        registrarCalificacionUk("33111222", "PADRON-UK-001", "CUR-MAT", "B", "B", "A", "2020-2021", "SECUNDARIA");
        registrarCalificacionUk("33111222", "PADRON-UK-001", "CUR-LIT", "A", "A", "A", "2021-2022", "SECUNDARIA");
        registrarCalificacionUk("33111222", "PADRON-UK-001", "CUR-BIO", "C", "B", "B", "2021-2022", "SECUNDARIA");

        // Alemania
        registrarCalificacionAlemania("32111222", "PADRON-DE-001", "CUR-CAR", 2.3, 2.0, "2022-2023", "SECUNDARIA");
        registrarCalificacionAlemania("32111222", "PADRON-DE-001", "CUR-QUI", 1.7, 2.3, "2022-2023", "SECUNDARIA");
        registrarCalificacionAlemania("32111222", "PADRON-DE-001", "CUR-INF", 2.0, 1.7, "2023-2024", "SECUNDARIA");
        registrarCalificacionAlemania("32111222", "PADRON-DE-001", "CUR-GIM", 2.7, 2.3, "2023-2024", "SECUNDARIA");

        // Sudafrica
        registrarCalificacionSudafrica("29111222", "PADRON-ZA-001", "CUR-MAT", 72, 78, 81, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("29111222", "PADRON-ZA-001", "CUR-INF", 80, 76, 84, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("29111222", "PADRON-ZA-001", "CUR-FIS", 75, 73, 79, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("28111222", "PADRON-ZA-002", "CUR-QUI", 68, 74, 70, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("28111222", "PADRON-ZA-002", "CUR-FIS", 75, 79, 77, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("28111222", "PADRON-ZA-002", "CUR-INF", 82, 80, 85, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("27111222", "PADRON-ZA-003", "CUR-MAT", 70, 72, 75, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("27111222", "PADRON-ZA-003", "CUR-LIT", 74, 76, 73, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("27111222", "PADRON-ZA-003", "CUR-ART", 78, 80, 82, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("26111222", "PADRON-ZA-004", "CUR-MAT", 66, 71, 69, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("26111222", "PADRON-ZA-004", "CUR-BIO", 73, 75, 74, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("26111222", "PADRON-ZA-004", "CUR-QUI", 77, 79, 78, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("25111222", "PADRON-ZA-005", "CUR-FIS", 81, 84, 83, "2024-2025", "UNIVERSIDAD");
        registrarCalificacionSudafrica("25111222", "PADRON-ZA-005", "CUR-INF", 86, 88, 87, "2024-2025", "UNIVERSIDAD");
        registrarCalificacionSudafrica("25111222", "PADRON-ZA-005", "CUR-MAT", 79, 82, 80, "2024-2025", "UNIVERSIDAD");
        registrarCalificacionSudafrica("24111222", "PADRON-ZA-006", "CUR-MAT", 69, 70, 72, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("24111222", "PADRON-ZA-006", "CUR-LIT", 72, 74, 73, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("24111222", "PADRON-ZA-006", "CUR-GIM", 80, 82, 81, "2024-2025", "PRIMARIA");
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

    private Institucion insertarInstitucion(String padron, String nombre, String pais, String provincia, String nivelEducativo, Materia... curriculum) {
        Institucion institucion = new Institucion();
        institucion.setId(padron);
        institucion.setNombre(nombre);
        institucion.setPais(pais);
        institucion.setProvincia(provincia);
        institucion.setNivelEducativo(nivelEducativo);
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

    private void crearEstudianteViaApi(String idNacional, String nombre, String pais, String institucionActual, String email) {
        RequestRegistrarEstudiante request = new RequestRegistrarEstudiante();
        request.setIdNacional(idNacional);
        request.setNombre(nombre);
        request.setPaisOrigen(pais);
        request.setInstitucionActual(institucionActual);
        request.setEmail(email);
        estudianteService.registrarEstudiante(request);
    }

    private void registrarCalificacionArgentina(
            String estudianteId,
            String institucionId,
            String materiaId,
            int primerParcial,
            int segundoParcial,
            int examenFinal,
            String periodo,
            String nivel) {
        RequestRegistrarCalificacion request = new RequestRegistrarCalificacion();
        request.setEstudiante(estudianteId);
        request.setInstitucion(institucionId);
        request.setMateria(materiaId);
        request.setPaisOrigen("Argentina");

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("primer_parcial", primerParcial);
        metadatos.put("segundo_parcial", segundoParcial);
        metadatos.put("examen_final", examenFinal);
        metadatos.put("periodo", periodo);
        metadatos.put("nivel", nivel);
        metadatos.put("fecha_normativa", "2018-03-10");
        request.setMetadatos(metadatos);

        calificacionService.registrarCalificacionOriginal(request);
    }

    private void registrarCalificacionUsa(
            String estudianteId,
            String institucionId,
            String materiaId,
            String semester1,
            String semester2,
            String periodo,
            String nivel) {
        RequestRegistrarCalificacion request = new RequestRegistrarCalificacion();
        request.setEstudiante(estudianteId);
        request.setInstitucion(institucionId);
        request.setMateria(materiaId);
        request.setPaisOrigen("USA");

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("semester", semester1);
        metadatos.put("semester_2", semester2);
        metadatos.put("periodo", periodo);
        metadatos.put("nivel", nivel);
        metadatos.put("fecha_normativa", "2018-03-10");
        request.setMetadatos(metadatos);

        calificacionService.registrarCalificacionOriginal(request);
    }

    private void registrarCalificacionUk(
            String estudianteId,
            String institucionId,
            String materiaId,
            String coursework,
            String mockExam,
            String finalGrade,
            String periodo,
            String nivel) {
        RequestRegistrarCalificacion request = new RequestRegistrarCalificacion();
        request.setEstudiante(estudianteId);
        request.setInstitucion(institucionId);
        request.setMateria(materiaId);
        request.setPaisOrigen("UK");

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("coursework", coursework);
        metadatos.put("mock_exam", mockExam);
        metadatos.put("final_grade", finalGrade);
        metadatos.put("periodo", periodo);
        metadatos.put("nivel", nivel);
        metadatos.put("fecha_normativa", "2018-03-10");
        request.setMetadatos(metadatos);

        calificacionService.registrarCalificacionOriginal(request);
    }

    private void registrarCalificacionAlemania(
            String estudianteId,
            String institucionId,
            String materiaId,
            double klassenArbeit,
            double mundlichArbeit,
            String periodo,
            String nivel) {
        RequestRegistrarCalificacion request = new RequestRegistrarCalificacion();
        request.setEstudiante(estudianteId);
        request.setInstitucion(institucionId);
        request.setMateria(materiaId);
        request.setPaisOrigen("Alemania");

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("KlassenArbeit", klassenArbeit);
        metadatos.put("MundlichArbeit", mundlichArbeit);
        metadatos.put("periodo", periodo);
        metadatos.put("nivel", nivel);
        metadatos.put("fecha_normativa", "2018-03-10");
        request.setMetadatos(metadatos);

        calificacionService.registrarCalificacionOriginal(request);
    }

    private void registrarCalificacionSudafrica(
            String estudianteId,
            String institucionId,
            String materiaId,
            double parcial,
            double trabajosPracticos,
            double examenFinal,
            String periodo,
            String nivel) {
        RequestRegistrarCalificacion request = new RequestRegistrarCalificacion();
        request.setEstudiante(estudianteId);
        request.setInstitucion(institucionId);
        request.setMateria(materiaId);
        request.setPaisOrigen("Sudafrica");

        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("parcial", parcial);
        metadatos.put("trabajos_practicos", trabajosPracticos);
        metadatos.put("final", examenFinal);
        metadatos.put("score", (parcial + trabajosPracticos + examenFinal) / 3.0);
        metadatos.put("periodo", periodo);
        metadatos.put("nivel", nivel);
        metadatos.put("fecha_normativa", "2018-03-10");
        request.setMetadatos(metadatos);

        calificacionService.registrarCalificacionOriginal(request);
    }
}
