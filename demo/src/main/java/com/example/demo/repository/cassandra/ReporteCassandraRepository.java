package com.example.demo.repository.cassandra;
import com.example.demo.model.Reporte;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface ReporteCassandraRepository extends CassandraRepository<Reporte, String> {
        @Query("SELECT * FROM reporte WHERE tipo_ranking = ?0 LIMIT 1")
        Optional<Reporte> obtenerMejorPorTipo(String tipoReporte);
    }

