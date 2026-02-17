package com.example.demo.service;

import com.example.demo.model.Reporte;
import com.example.demo.model.ReportePromedio;
import com.example.demo.repository.cassandra.ReporteCassandraRepository;
import com.example.demo.repository.neo4j.EstudianteNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteServicio {

        @Autowired
        private EstudianteNeo4jRepository neo4jRepo;

        @Autowired
        private ReporteCassandraRepository cassandraRepo;

        public void sincronizarDatosAnaliticos() {
            List<ReportePromedio>promediosPais = neo4jRepo.calcularpromedioPorPais();
            List<ReportePromedio>promediosInst = neo4jRepo.calcularPromedioPorInstitucion();

            List<Reporte> batch = new ArrayList<>();
            promediosPais.forEach(m -> {
                batch.add(new Reporte("PAIS", m.getPromedio(),  m.getPais()));
            });

            promediosInst.forEach(m -> {
                batch.add(new Reporte("INSTITUTO",  m.getPromedio(),m.getNombre()));
            });

            cassandraRepo.saveAll(batch);
        }
    }
