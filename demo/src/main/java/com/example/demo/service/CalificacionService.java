package com.example.demo.service;


import com.example.demo.model.Calificacion;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CalificacionService {
    @Autowired
    private CalificacionMONGORepository calificacionRepository;

    public Calificacion registrarCalificacionOriginal(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion c = new Calificacion();
        c.setEstudianteId(requestRegistrarCalificacion.getEstudiante());
        c.setMateriaId(requestRegistrarCalificacion.getMateria());
        c.setPaisOrigen(requestRegistrarCalificacion.getPaisOrigen());
        c.setMetadata(requestRegistrarCalificacion.getMetadatos());
        c.setAuditor("auditor");
        c.setFechaProcesamiento(LocalDateTime.now());

        String resultadoSA = calcularConversionSudafrica(requestRegistrarCalificacion);
        c.getConversiones().put("sudafrica", resultadoSA);

        return calificacionRepository.save(c);
    }

    public String calcularConversionSudafrica(RequestRegistrarCalificacion request) {
        if (request == null) return "SIN_DATOS";

        String paisEnMinuscula = request.getPaisOrigen().toLowerCase();
        double resultado = 0;

        if (paisEnMinuscula.equals("argentina")) {
            Integer primer_parcial= (Integer) request.getMetadatos().get("primer_parcial");
            Integer segundo_parcial=(Integer) request.getMetadatos().get("segundo_parcial");
            Integer examen_final= (Integer) request.getMetadatos().get("examen_final");
            resultado = ((primer_parcial+segundo_parcial+examen_final)/3) * 10;
        }
        else if (paisEnMinuscula.equals("alemania")) {
            double klassenArbeit= (Double) request.getMetadatos().get("KlassenArbeit");
            double mundlichArbeit=(Double) request.getMetadatos().get("MundlichArbeit");
            resultado = ((5.0 - ((klassenArbeit+mundlichArbeit)/2)) * 25);
        }
        else if (paisEnMinuscula.equals("estados unidos") || paisEnMinuscula.equals("usa")) {
            String semester_original = (String) request.getMetadatos().get("semester");
            Integer semester = 0;
            if( semester_original.equals("A")){
                semester = 100;
            } else if (semester_original.equals("B")) {
                semester = 80;
            } else if (semester_original.equals("C")) {
                semester = 70;
            } else if (semester_original.equals("D")) {
                semester = 60;
            } else if (semester_original.equals("E")) {
                semester = 50;
            } else if (semester_original.equals( "F")) {
                semester = 0;
            }
            double gpa=(Integer) request.getMetadatos().get("gpa");
            double gpaConvertido = (gpa*100)/4;
            resultado = (gpaConvertido+semester)/2;

        }
        else if (paisEnMinuscula.equals("inglaterra") || paisEnMinuscula.equals("uk")) {
            double courseWork= (Double) request.getMetadatos().get("courseWork");
            double mockExam=(Double) request.getMetadatos().get("mockExam");
            double final_grade=(Double) request.getMetadatos().get("final_grade");
            resultado = ((courseWork+mockExam+final_grade)/ 9.0) * 100;
        }
        else {
            return "No se permite conversión";
        }

        return resultado + "%";
    }
}






