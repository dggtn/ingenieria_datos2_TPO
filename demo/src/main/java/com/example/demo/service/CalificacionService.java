package com.example.demo.service;


import com.example.demo.model.*;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CalificacionService {
    @Autowired
    private CalificacionMONGORepository calificacionRepository;
    @Autowired
    private EstudianteNeo4jRepository neo4jRepository;
    @Autowired
    private ReporteCassandraRepository cassandraRepository;
    private Double aDouble(Object valor) {
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
    public void actualizarRankingsNacionales() {
        List<ReportePromedio> promediosPais = neo4jRepository.calcularpromedioPorPais();
        List<ReportePromedio> promediosInst = neo4jRepository.calcularPromedioPorInstitucion();

        List<Reporte> listaParaCassandra = new ArrayList<>();

        promediosPais.forEach(m -> listaParaCassandra.add(new Reporte("PAIS", m.getPromedio(), m.getNombre())));
        promediosInst.forEach(m -> listaParaCassandra.add(new Reporte("INSTITUTO", m.getPromedio(), m.getNombre())));

        cassandraRepository.saveAll(listaParaCassandra);
    }
    public Calificacion registrarCalificacionOriginal(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion c = new Calificacion();
        c.setEstudianteId(requestRegistrarCalificacion.getEstudiante());
        c.setMateriaId(requestRegistrarCalificacion.getMateria());
        c.setPaisOrigen(requestRegistrarCalificacion.getPaisOrigen());
        c.setMetadata(requestRegistrarCalificacion.getMetadatos());
        c.setAuditor("auditor");
        c.setFechaProcesamiento(LocalDateTime.now());

        Double resultadoSA = calcularConversionSudafrica(requestRegistrarCalificacion);
        c.setConversiones(resultadoSA);

        UUID idEstudiante = UUID.fromString(c.getEstudianteId());
        UUID idInstitucion = UUID.fromString(requestRegistrarCalificacion.getInstitucion());
        UUID idMateria = UUID.fromString(requestRegistrarCalificacion.getMateria());
        String periodo  = (String) requestRegistrarCalificacion.getMetadatos().get("periodo");
        String nivel  = (String) requestRegistrarCalificacion.getMetadatos().get("nivel");
        Double promedio = calcularConversionSudafrica(requestRegistrarCalificacion);
        String pais =  requestRegistrarCalificacion.getPaisOrigen();

        neo4jRepository.registrarDondeEstudio(idEstudiante, idInstitucion, periodo);
        neo4jRepository.registrarCursada(idEstudiante,idMateria,promedio,periodo,nivel);
        neo4jRepository.registrarDondeDictaMateria(idMateria,idInstitucion,periodo,nivel);
        return calificacionRepository.save(c);
    }



    public Double calcularConversionSudafrica(RequestRegistrarCalificacion request) {
        String paisEnMinuscula = request.getPaisOrigen().toLowerCase();
        double resultado = 0;

        if (paisEnMinuscula.equals("argentina")) {
            Integer primer_parcial = (Integer) request.getMetadatos().get("primer_parcial");
            Integer segundo_parcial = (Integer) request.getMetadatos().get("segundo_parcial");
            Integer examen_final = (Integer) request.getMetadatos().get("examen_final");
            resultado = ((primer_parcial + segundo_parcial + examen_final) / 3) * 10;
        } else if (paisEnMinuscula.equals("alemania")) {
            double klassenArbeit = aDouble(request.getMetadatos().get("KlassenArbeit"));
            double mundlichArbeit = aDouble(request.getMetadatos().get("MundlichArbeit"));
            resultado = ((5.0 - ((klassenArbeit + mundlichArbeit) / 2)) * 25);
        } else if (paisEnMinuscula.equals("estados unidos") || paisEnMinuscula.equals("usa")) {
            String semester_original = (String) request.getMetadatos().get("semester");
            Integer semester = 0;
            if (semester_original.equals("A")) {
                semester = 100;
            } else if (semester_original.equals("B")) {
                semester = 80;
            } else if (semester_original.equals("C")) {
                semester = 70;
            } else if (semester_original.equals("D")) {
                semester = 60;
            } else if (semester_original.equals("E")) {
                semester = 50;
            } else if (semester_original.equals("F")) {
                semester = 0;
            }
            double gpa = (Integer) request.getMetadatos().get("gpa");
            double gpaConvertido = (gpa * 100) / 4;
            resultado = (gpaConvertido + semester) / 2;

        } else if (paisEnMinuscula.equals("inglaterra") || paisEnMinuscula.equals("uk")) {
            String courseWork = (String) request.getMetadatos().get("coursework");
            String mockExam = (String) request.getMetadatos().get("mock_exam");
            String final_grade = (String) request.getMetadatos().get("final_grade");

            int courseWorkConverted = convertir(courseWork);
            int mock = convertir(mockExam);
            int grade = convertir(final_grade);


            Integer total = courseWorkConverted + mock + grade;
            resultado = total / 3;
        }
        return resultado ;
    }


    private int convertir(String valor) {
        int nuevoValor = 0;
        if (valor.equals("A")) {
            nuevoValor = 100;
        } else if (valor.equals("B")) {
            nuevoValor = 90;
        } else if (valor.equals("C")) {
            nuevoValor = 80;
        }
        else if (valor.equals("D")) {
            nuevoValor = 70;
        }
        else if (valor.equals("E")) {
            nuevoValor = 60;
        }
        return nuevoValor;
    }
}






