package com.example.demo.service;

import com.example.demo.model.Calificacion;
import com.example.demo.model.ReportePromedio;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.mongo.CalificacionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReporteServcio {

        @Autowired
        private CalificacionMONGORepository mongoRepo;

        @Autowired
        private ReporteCassandraRepository cassandraRepo;

        public ReportePromedio generarReporteOficial(String pais, Date fecha) {
            List<Calificacion> notas = mongoRepo.findByPaisOrigen(pais);

            double promedio = notas.stream()
                    .mapToDouble(n -> Double.parseDouble(n.getNotaOriginal()))
                    .average()
                    .orElse(0.0);

            ReportePromedio reporte = new ReportePromedio();
            reporte.setPromedio(promedio);
            cassandraRepo.save(reporte);
            return reporte;
        }

    }

