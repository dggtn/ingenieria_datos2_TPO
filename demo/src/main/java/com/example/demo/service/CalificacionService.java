package com.example.demo.service;


import com.example.demo.model.Calificacion;
import com.example.demo.model.RequestRegistrarCalificacion;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CalificacionService {
    @Autowired
    private CalificacionMONGORepository calificacionRepository;

    public Calificacion registrarCalificacionOriginal(RequestRegistrarCalificacion requestRegistrarCalificacion) {
        Calificacion c = new Calificacion();
        c.setEstudianteId(requestRegistrarCalificacion.getEstudiante());
        c.setMateriaId(requestRegistrarCalificacion.getMateria());
        c.setPaisOrigen(requestRegistrarCalificacion.getPaisOrigen());
        c.setDetallesOriginales(requestRegistrarCalificacion.getMetadatos());
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
            double primer_parcial= (Double) request.getMetadatos().get("primer_parcial");
            double segundo_parcial=(Double) request.getMetadatos().get("segundo_parcial");
            double examen_final= (Double) request.getMetadatos().get("examen_final");
            resultado = (primer_parcial+segundo_parcial+examen_final/3) * 10;
        }
        else if (paisEnMinuscula.equals("alemania")) {
            double klassenArbeit= (Double) request.getMetadatos().get("KlassenArbeit");
            double mundlichArbeit=(Double) request.getMetadatos().get("MundlichArbeit");
            resultado = ((5.0 - ((klassenArbeit+mundlichArbeit)/2)) * 25);
        }
        else if (paisEnMinuscula.equals("estados unidos") || paisEnMinuscula.equals("usa")) {
            double semester= (Double) request.getMetadatos().get("semester");
            double gpa=(Double) request.getMetadatos().get("gpa");
            resultado = (((semester+gpa)/2) / 4.0) * 100;
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






