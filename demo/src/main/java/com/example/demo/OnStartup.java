package com.example.demo;

import com.example.demo.model.CursoMateria;
import com.example.demo.model.Estudiante;
import com.example.demo.model.EstudioEn;
import com.example.demo.model.Institucion;
import com.example.demo.model.LegislacionConversion;
import com.example.demo.model.Materia;
import com.example.demo.model.RequestRegistrarEquivalenciaMateria;
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
import com.example.demo.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    @Autowired
    private MateriaService materiaService;

    private Random random = new Random();

    private Map<String, List<Estudiante>> estudiantesPorPais;
    private Map<String, List<Institucion>> institucionesPorPais;

    @Value("${numero.registros}")
    Integer registros;
    @Value("${dbInicializarDatos}")
    boolean inicializar;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(!inicializar)return;

        inicializarMapas();

        materiaRepo.deleteAll();
        institucionRepo.deleteAll();
        estudiantesRepo.deleteAll();
        estudianteMongoRepo.deleteAll();
        calificacionMongoRepo.deleteAll();
        calificacionCassandraRepo.deleteAll();

        seedLegislacionConversion();

        // MATERIAS POR INSTITUCION (NO COMPARTIDAS ENTRE INSTITUCIONES)
        // PADRON-001
        Materia p1Matematica = insertarMateria(mid("PADRON-001", "CUR-MAT"), "MATEMATICA");
        Materia p1Literatura = insertarMateria(mid("PADRON-001", "CUR-LIT"), "LITERATURA");
        Materia p1Biologia = insertarMateria(mid("PADRON-001", "CUR-BIO"), "BIOLOGIA");
        Materia p1Arte = insertarMateria(mid("PADRON-001", "CUR-ART"), "ARTE");
        Materia p1Informatica = insertarMateria(mid("PADRON-001", "CUR-INF"), "INFORMATICA");
        Materia p1Fisica = insertarMateria(mid("PADRON-001", "CUR-FIS"), "FISICA");

        // PADRON-002
        Materia p2Matematica = insertarMateria(mid("PADRON-002", "CUR-MAT"), "MATEMATICA");
        Materia p2Quimica = insertarMateria(mid("PADRON-002", "CUR-QUI"), "QUIMICA");
        Materia p2Futbol = insertarMateria(mid("PADRON-002", "CUR-FUT"), "FUTBOL");
        Materia p2Gimnasia = insertarMateria(mid("PADRON-002", "CUR-GIM"), "GIMNASIA");
        Materia p2Carpinteria = insertarMateria(mid("PADRON-002", "CUR-CAR"), "CARPINTERIA");
        Materia p2Informatica = insertarMateria(mid("PADRON-002", "CUR-INF"), "INFORMATICA");

        // PADRON-UK-001
        Materia ukMatematica = insertarMateria(mid("PADRON-UK-001", "CUR-MAT"), "MATEMATICA");
        Materia ukLiteratura = insertarMateria(mid("PADRON-UK-001", "CUR-LIT"), "LITERATURA");
        Materia ukBiologia = insertarMateria(mid("PADRON-UK-001", "CUR-BIO"), "BIOLOGIA");
        Materia ukArte = insertarMateria(mid("PADRON-UK-001", "CUR-ART"), "ARTE");
        Materia ukInformatica = insertarMateria(mid("PADRON-UK-001", "CUR-INF"), "INFORMATICA");
        Materia ukFisica = insertarMateria(mid("PADRON-UK-001", "CUR-FIS"), "FISICA");

        // PADRON-DE-001
        Materia deMatematica = insertarMateria(mid("PADRON-DE-001", "CUR-MAT"), "MATEMATICA");
        Materia deQuimica = insertarMateria(mid("PADRON-DE-001", "CUR-QUI"), "QUIMICA");
        Materia deFisica = insertarMateria(mid("PADRON-DE-001", "CUR-FIS"), "FISICA");
        Materia deInformatica = insertarMateria(mid("PADRON-DE-001", "CUR-INF"), "INFORMATICA");
        Materia deCarpinteria = insertarMateria(mid("PADRON-DE-001", "CUR-CAR"), "CARPINTERIA");
        Materia deGimnasia = insertarMateria(mid("PADRON-DE-001", "CUR-GIM"), "GIMNASIA");

        // PADRON-ZA-001
        Materia za1Matematica = insertarMateria(mid("PADRON-ZA-001", "CUR-MAT"), "MATEMATICA");
        Materia za1Biologia = insertarMateria(mid("PADRON-ZA-001", "CUR-BIO"), "BIOLOGIA");
        Materia za1Fisica = insertarMateria(mid("PADRON-ZA-001", "CUR-FIS"), "FISICA");
        Materia za1Informatica = insertarMateria(mid("PADRON-ZA-001", "CUR-INF"), "INFORMATICA");
        Materia za1Literatura = insertarMateria(mid("PADRON-ZA-001", "CUR-LIT"), "LITERATURA");

        // PADRON-ZA-002
        Materia za2Quimica = insertarMateria(mid("PADRON-ZA-002", "CUR-QUI"), "QUIMICA");
        Materia za2Fisica = insertarMateria(mid("PADRON-ZA-002", "CUR-FIS"), "FISICA");
        Materia za2Informatica = insertarMateria(mid("PADRON-ZA-002", "CUR-INF"), "INFORMATICA");
        Materia za2Carpinteria = insertarMateria(mid("PADRON-ZA-002", "CUR-CAR"), "CARPINTERIA");
        Materia za2Gimnasia = insertarMateria(mid("PADRON-ZA-002", "CUR-GIM"), "GIMNASIA");

        // PADRON-ZA-003
        Materia za3Matematica = insertarMateria(mid("PADRON-ZA-003", "CUR-MAT"), "MATEMATICA");
        Materia za3Literatura = insertarMateria(mid("PADRON-ZA-003", "CUR-LIT"), "LITERATURA");
        Materia za3Arte = insertarMateria(mid("PADRON-ZA-003", "CUR-ART"), "ARTE");
        Materia za3Biologia = insertarMateria(mid("PADRON-ZA-003", "CUR-BIO"), "BIOLOGIA");

        // PADRON-ZA-004
        Materia za4Matematica = insertarMateria(mid("PADRON-ZA-004", "CUR-MAT"), "MATEMATICA");
        Materia za4Quimica = insertarMateria(mid("PADRON-ZA-004", "CUR-QUI"), "QUIMICA");
        Materia za4Fisica = insertarMateria(mid("PADRON-ZA-004", "CUR-FIS"), "FISICA");
        Materia za4Informatica = insertarMateria(mid("PADRON-ZA-004", "CUR-INF"), "INFORMATICA");
        Materia za4Biologia = insertarMateria(mid("PADRON-ZA-004", "CUR-BIO"), "BIOLOGIA");

        // PADRON-ZA-005
        Materia za5Quimica = insertarMateria(mid("PADRON-ZA-005", "CUR-QUI"), "QUIMICA");
        Materia za5Fisica = insertarMateria(mid("PADRON-ZA-005", "CUR-FIS"), "FISICA");
        Materia za5Informatica = insertarMateria(mid("PADRON-ZA-005", "CUR-INF"), "INFORMATICA");
        Materia za5Matematica = insertarMateria(mid("PADRON-ZA-005", "CUR-MAT"), "MATEMATICA");
        Materia za5Literatura = insertarMateria(mid("PADRON-ZA-005", "CUR-LIT"), "LITERATURA");

        // PADRON-ZA-006
        Materia za6Matematica = insertarMateria(mid("PADRON-ZA-006", "CUR-MAT"), "MATEMATICA");
        Materia za6Literatura = insertarMateria(mid("PADRON-ZA-006", "CUR-LIT"), "LITERATURA");
        Materia za6Arte = insertarMateria(mid("PADRON-ZA-006", "CUR-ART"), "ARTE");
        Materia za6Gimnasia = insertarMateria(mid("PADRON-ZA-006", "CUR-GIM"), "GIMNASIA");

        // INSTITUCIONES CON CURRICULUM
        Institucion uade = insertarInstitucion(
                "PADRON-001",
                "Colegio Nuevo Milenio",
                "Argentina",
                "Buenos Aires",
                "Secundaria",
                p1Matematica, p1Literatura, p1Biologia, p1Arte, p1Informatica, p1Fisica);

        institucionesPorPais.get("ARGENTINA").add(uade);

        Institucion unlam = insertarInstitucion(
                "PADRON-002",
                "Colegio 3 de febrero",
                "USA",
                "California",
                "Secundaria",
                p2Matematica, p2Quimica, p2Futbol, p2Gimnasia, p2Carpinteria, p2Informatica);

        institucionesPorPais.get("USA").add(unlam);

        Institucion ukLondon = insertarInstitucion(
                "PADRON-UK-001",
                "London College of Sciences",
                "UK",
                "England",
                "Secundaria",
                ukMatematica, ukLiteratura, ukBiologia, ukArte, ukInformatica, ukFisica);

        institucionesPorPais.get("UK").add(ukLondon);

        Institucion deBerlin = insertarInstitucion(
                "PADRON-DE-001",
                "Berlin Technische Schule",
                "Alemania",
                "Berlin",
                "Secundaria",
                deMatematica, deQuimica, deFisica, deInformatica, deCarpinteria, deGimnasia);
        institucionesPorPais.get("ALEMANIA").add(deBerlin);

        Institucion zaCapeTown = insertarInstitucion(
                "PADRON-ZA-001",
                "Cape Town Central School",
                "Sudafrica",
                "Western Cape",
                "Secundaria",
                za1Matematica, za1Biologia, za1Fisica, za1Informatica, za1Literatura);
        Institucion zaJohannesburg = insertarInstitucion(
                "PADRON-ZA-002",
                "Johannesburg Technical College",
                "Sudafrica",
                "Gauteng",
                "Universidad",
                za2Quimica, za2Fisica, za2Informatica, za2Carpinteria, za2Gimnasia);
        Institucion zaPretoriaPrimary = insertarInstitucion(
                "PADRON-ZA-003",
                "Pretoria Primary Academy",
                "Sudafrica",
                "Gauteng",
                "Primaria",
                za3Matematica, za3Literatura, za3Arte, za3Biologia);
        Institucion zaDurbanHigh = insertarInstitucion(
                "PADRON-ZA-004",
                "Durban Coast High School",
                "Sudafrica",
                "KwaZulu-Natal",
                "Secundaria",
                za4Matematica, za4Quimica, za4Fisica, za4Informatica, za4Biologia);
        Institucion zaBloemfonteinUni = insertarInstitucion(
                "PADRON-ZA-005",
                "Bloemfontein Science University",
                "Sudafrica",
                "Free State",
                "Universidad",
                za5Quimica, za5Fisica, za5Informatica, za5Matematica, za5Literatura);
        Institucion zaPortElizabethPrimary = insertarInstitucion(
                "PADRON-ZA-006",
                "Port Elizabeth Learning Center",
                "Sudafrica",
                "Eastern Cape",
                "Primaria",
                za6Matematica, za6Literatura, za6Arte, za6Gimnasia);

        // UNIVERSIDADES PARA PRUEBA DE TRANSFERENCIA INTERNACIONAL (USA -> SUDAFRICA)
        Materia usUniCalculus = insertarMateria(mid("PADRON-US-UNI-01", "UNI-CALC-101"), "Calculus I");
        Materia usUniPhysics = insertarMateria(mid("PADRON-US-UNI-01", "UNI-PHY-101"), "Physics I");
        Materia usUniProgramming = insertarMateria(mid("PADRON-US-UNI-01", "UNI-CS-101"), "Programming Fundamentals");
        Materia usUniEconomics = insertarMateria(mid("PADRON-US-UNI-01", "UNI-ECO-101"), "Economics I");
        Materia usUniHistory = insertarMateria(mid("PADRON-US-UNI-01", "UNI-HIS-101"), "World History");

        Materia zaUniMathematics = insertarMateria(mid("PADRON-ZA-UNI-07", "UNI-MATH-101"), "Mathematics I");
        Materia zaUniMechanics = insertarMateria(mid("PADRON-ZA-UNI-07", "UNI-MECH-101"), "Engineering Mechanics");
        Materia zaUniSoftware = insertarMateria(mid("PADRON-ZA-UNI-07", "UNI-SWE-101"), "Software Development I");
        Materia zaUniBusiness = insertarMateria(mid("PADRON-ZA-UNI-07", "UNI-BUS-101"), "Business Foundations");
        Materia zaUniAnthropology = insertarMateria(mid("PADRON-ZA-UNI-07", "UNI-ANTH-101"), "Social Anthropology");

        Institucion usTransferUniversity = insertarInstitucion(
                "PADRON-US-UNI-01",
                "Pacific State University",
                "USA",
                "California",
                "Universidad",
                usUniCalculus, usUniPhysics, usUniProgramming, usUniEconomics, usUniHistory);
        institucionesPorPais.get("USA").add(usTransferUniversity);

        Institucion zaTransferUniversity = insertarInstitucion(
                "PADRON-ZA-UNI-07",
                "Cape Peninsula University of Technology",
                "Sudafrica",
                "Western Cape",
                "Universidad",
                zaUniMathematics, zaUniMechanics, zaUniSoftware, zaUniBusiness, zaUniAnthropology);

        // EQUIVALENCIAS PARCIALES (SOLO ALGUNAS MATERIAS)
        registrarEquivalenciaUniversitaria("PADRON-US-UNI-01", usUniCalculus.getId(), "PADRON-ZA-UNI-07", zaUniMathematics.getId());
        registrarEquivalenciaUniversitaria("PADRON-US-UNI-01", usUniPhysics.getId(), "PADRON-ZA-UNI-07", zaUniMechanics.getId());
        registrarEquivalenciaUniversitaria("PADRON-US-UNI-01", usUniProgramming.getId(), "PADRON-ZA-UNI-07", zaUniSoftware.getId());

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

        // ESTUDIANTES USA PARA PRUEBA DE TRANSFERENCIA CON AVANCE PARCIAL
        crearEstudianteViaApi("37111222", "Emily Carter", "USA", "PADRON-US-UNI-01", "emily.carter@mail.com");
        crearEstudianteViaApi("38111222", "Noah Williams", "USA", "PADRON-US-UNI-01", "noah.williams@mail.com");
        crearEstudianteViaApi("39111222", "Sophia Johnson", "USA", "PADRON-US-UNI-01", "sophia.johnson@mail.com");

        // Argentina
//        registrarCalificacionArgentina("35111222", "PADRON-001", p1Matematica.getId(), 9, 8, 10, "2023-2024", "SECUNDARIA");
//        registrarCalificacionArgentina("35111222", "PADRON-001", p1Literatura.getId(), 8, 9, 8, "2023-2024", "SECUNDARIA");
//        registrarCalificacionArgentina("31111222", "PADRON-001", p1Biologia.getId(), 7, 8, 9, "2023-2024", "SECUNDARIA");
//        registrarCalificacionArgentina("31111222", "PADRON-001", p1Fisica.getId(), 8, 7, 8, "2023-2024", "SECUNDARIA");

        // USA
//        registrarCalificacionUsa("34111222", "PADRON-002", p2Quimica.getId(), "B", "A", "2018-2019", "SECUNDARIA");
//        registrarCalificacionUsa("34111222", "PADRON-002", p2Informatica.getId(), "A", "B", "2018-2019", "SECUNDARIA");
//        registrarCalificacionUsa("30111222", "PADRON-002", p2Gimnasia.getId(), "C", "B", "2019-2020", "SECUNDARIA");
//        registrarCalificacionUsa("30111222", "PADRON-002", p2Futbol.getId(), "B", "B", "2019-2020", "SECUNDARIA");

        // UK
//        registrarCalificacionUk("33111222", "PADRON-UK-001", ukArte.getId(), "A", "B", "A*", "2020-2021", "SECUNDARIA");
//        registrarCalificacionUk("33111222", "PADRON-UK-001", ukMatematica.getId(), "B", "B", "A", "2020-2021", "SECUNDARIA");
//        registrarCalificacionUk("33111222", "PADRON-UK-001", ukLiteratura.getId(), "A", "A", "A", "2021-2022", "SECUNDARIA");
//        registrarCalificacionUk("33111222", "PADRON-UK-001", ukBiologia.getId(), "C", "B", "B", "2021-2022", "SECUNDARIA");

        // Alemania
//        registrarCalificacionAlemania("32111222", "PADRON-DE-001", deCarpinteria.getId(), 2.3, 2.0, "2022-2023", "SECUNDARIA");
//        registrarCalificacionAlemania("32111222", "PADRON-DE-001", deQuimica.getId(), 1.7, 2.3, "2022-2023", "SECUNDARIA");
//        registrarCalificacionAlemania("32111222", "PADRON-DE-001", deInformatica.getId(), 2.0, 1.7, "2023-2024", "SECUNDARIA");
//        registrarCalificacionAlemania("32111222", "PADRON-DE-001", deGimnasia.getId(), 2.7, 2.3, "2023-2024", "SECUNDARIA");

        // Sudafrica
        registrarCalificacionSudafrica("29111222", "PADRON-ZA-001", za1Matematica.getId(), 72, 78, 81, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("29111222", "PADRON-ZA-001", za1Informatica.getId(), 80, 76, 84, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("29111222", "PADRON-ZA-001", za1Fisica.getId(), 75, 73, 79, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("28111222", "PADRON-ZA-002", za2Quimica.getId(), 68, 74, 70, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("28111222", "PADRON-ZA-002", za2Fisica.getId(), 75, 79, 77, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("28111222", "PADRON-ZA-002", za2Informatica.getId(), 82, 80, 85, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("27111222", "PADRON-ZA-003", za3Matematica.getId(), 70, 72, 75, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("27111222", "PADRON-ZA-003", za3Literatura.getId(), 74, 76, 73, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("27111222", "PADRON-ZA-003", za3Arte.getId(), 78, 80, 82, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("26111222", "PADRON-ZA-004", za4Matematica.getId(), 66, 71, 69, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("26111222", "PADRON-ZA-004", za4Biologia.getId(), 73, 75, 74, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("26111222", "PADRON-ZA-004", za4Quimica.getId(), 77, 79, 78, "2024-2025", "SECUNDARIA");
        registrarCalificacionSudafrica("25111222", "PADRON-ZA-005", za5Fisica.getId(), 81, 84, 83, "2024-2025", "UNIVERSIDAD");
        registrarCalificacionSudafrica("25111222", "PADRON-ZA-005", za5Informatica.getId(), 86, 88, 87, "2024-2025", "UNIVERSIDAD");
        registrarCalificacionSudafrica("25111222", "PADRON-ZA-005", za5Matematica.getId(), 79, 82, 80, "2024-2025", "UNIVERSIDAD");
        registrarCalificacionSudafrica("24111222", "PADRON-ZA-006", za6Matematica.getId(), 69, 70, 72, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("24111222", "PADRON-ZA-006", za6Literatura.getId(), 72, 74, 73, "2024-2025", "PRIMARIA");
        registrarCalificacionSudafrica("24111222", "PADRON-ZA-006", za6Gimnasia.getId(), 80, 82, 81, "2024-2025", "PRIMARIA");

         //USA UNIVERSIDAD - AVANCE PARCIAL (NO COMPLETAN TODO EL CURRICULUM)
        // Emily: 2 equivalentes + 1 no equivalente, faltan materias por cerrar.
        registrarCalificacionUsa("37111222", "PADRON-US-UNI-01", usUniCalculus.getId(), "A", "B", "2024-FALL", "UNIVERSIDAD");
        registrarCalificacionUsa("37111222", "PADRON-US-UNI-01", usUniProgramming.getId(), "A", "A", "2024-FALL", "UNIVERSIDAD");
        registrarCalificacionUsa("37111222", "PADRON-US-UNI-01", usUniHistory.getId(), "B", "B", "2024-FALL", "UNIVERSIDAD");

        // Noah: 1 equivalente + 1 no equivalente.
        registrarCalificacionUsa("38111222", "PADRON-US-UNI-01", usUniPhysics.getId(), "B", "A", "2024-FALL", "UNIVERSIDAD");
        registrarCalificacionUsa("38111222", "PADRON-US-UNI-01", usUniEconomics.getId(), "A", "B", "2024-FALL", "UNIVERSIDAD");

         //Sophia: 2 equivalentes + 1 no equivalente.
        registrarCalificacionUsa("39111222", "PADRON-US-UNI-01", usUniCalculus.getId(), "B", "B", "2024-FALL", "UNIVERSIDAD");
        registrarCalificacionUsa("39111222", "PADRON-US-UNI-01", usUniPhysics.getId(), "A", "A", "2024-FALL", "UNIVERSIDAD");
        registrarCalificacionUsa("39111222", "PADRON-US-UNI-01", usUniEconomics.getId(), "C", "B", "2024-FALL", "UNIVERSIDAD");

         //GENERACION N REGISTROS
        script(registros);
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

    private String mid(String institucionId, String codigoBase) {
        return institucionId + "::" + codigoBase;
    }

    private void registrarEquivalenciaUniversitaria(
            String institucionOrigenId,
            String materiaOrigenId,
            String institucionDestinoId,
            String materiaDestinoId) {
        RequestRegistrarEquivalenciaMateria request = new RequestRegistrarEquivalenciaMateria();
        request.setInstitucionOrigenId(institucionOrigenId);
        request.setMateriaOrigenId(materiaOrigenId);
        request.setInstitucionDestinoId(institucionDestinoId);
        request.setMateriaDestinoId(materiaDestinoId);
        materiaService.registrarEquivalencia(request);
    }

    private void crearEstudianteViaApi(String idNacional, String nombre, String pais, String institucionActual, String email) {
        RequestRegistrarEstudiante request = new RequestRegistrarEstudiante();
        request.setIdNacional(idNacional);
        request.setNombre(nombre);
        request.setPaisOrigen(pais);
        request.setInstitucionActual(institucionActual);
        request.setEmail(email);
        Estudiante e = estudianteService.registrarEstudiante(request);
        String paisEnMayusculas = pais.toUpperCase();

        if (paisEnMayusculas.equals("SUDAFRICA")) return;

        estudiantesPorPais.get(paisEnMayusculas).add(e);
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

    private void script (int cota) {
        int registros = 0;

        while(registros < cota) {
            float r = random.nextFloat();
            if (r <= 0.25) {
                registros += insertarCalificaciones("ARGENTINA", () -> {
                    int primerParcial= random.nextInt(4, 10);
                    int segundoParcial= random.nextInt(4, 10);
                    int examenFinal = random.nextInt(4, 10);
                    return new HashMap<>(
                            Map.of(
                                    "primer_parcial", primerParcial,
                                    "segundo_parcial", segundoParcial,
                                    "examen_final", examenFinal
                            )
                    );
                });
            } else if (r > 0.25 && r <= 0.5) {
                registros += insertarCalificaciones("ALEMANIA", () -> {
                    double klassenArbeit = random.nextDouble(1.0, 5.0);
                    double mundlichArbeit=random.nextDouble(1.0, 5.0);
                    return new HashMap<>(
                            Map.of(
                                    "KlassenArbeit", klassenArbeit,
                                    "MundlichArbeit", mundlichArbeit
                            )
                    );
                });
            } else if (r > 0.5 && r <= 0.75) {
                registros += insertarCalificaciones("UK", () -> {
                    String[] notasPosibles = new String[]{"A*", "A","B","C","D","E","F"};
                    int indiceNota1 = random.nextInt(notasPosibles.length);
                    int indiceNota2 = random.nextInt(notasPosibles.length);
                    int indiceNota3 = random.nextInt(notasPosibles.length);
                    return new HashMap<>(
                            Map.of(
                                    "coursework", notasPosibles[indiceNota1],
                                    "mock_exam", notasPosibles[indiceNota2],
                                    "final_grade", notasPosibles[indiceNota3]
                            )
                    );
                });
            } else {
                registros += insertarCalificaciones("USA", () -> {
                    String letra= "ABCDEF";
                    int indiceNota1 = random.nextInt(letra.length());
                    int indiceNota2 = random.nextInt(letra.length());
                    return new HashMap<>(
                            Map.of(
                                    "semester", String.valueOf(letra.charAt(indiceNota1)),
                                    "semester_2", String.valueOf(letra.charAt(indiceNota2))
                            )
                    );
                });
            }
        }
    }

    private int insertarCalificaciones(String pais, Supplier<Map<String, Object>> generadorMetadatos) {
        AtomicInteger contador = new AtomicInteger(0);
        Institucion institucion = obtenerEntidadRandom(institucionesPorPais.get(pais));
        Estudiante estudiante = obtenerEntidadRandom(estudiantesPorPais.get(pais));

        String estudianteId = estudiante.getId();
        String institucionId = institucion.getId();

        institucion.getCurriculum().forEach(m -> {
            RequestRegistrarCalificacion request = new RequestRegistrarCalificacion();
            request.setEstudiante(estudianteId);
            request.setInstitucion(institucionId);
            request.setMateria(m.getId());
            request.setPaisOrigen(institucion.getPais());
            request.setMetadatos(generadorMetadatos.get());
            calificacionService.registrarCalificacionOriginal(request);
            contador.incrementAndGet();
        });

        return contador.get();
    }

    private <T> T obtenerEntidadRandom(List<T> entidad) {
        return entidad.get(random.nextInt(entidad.size()));
    }


    private void inicializarMapas() {
        this.estudiantesPorPais = new HashMap<>(
                Map.of(
                        "ARGENTINA", new LinkedList<>(),
                        "ALEMANIA", new LinkedList<>(),
                        "UK", new LinkedList<>(),
                        "USA", new LinkedList<>()
                ));
        this.institucionesPorPais = new HashMap<>(
                Map.of(
                        "ARGENTINA", new LinkedList<>(),
                        "ALEMANIA", new LinkedList<>(),
                        "UK", new LinkedList<>(),
                        "USA", new LinkedList<>()
                ));
    }
}


