package com.example.demo.repository.cassandra;
import com.example.demo.model.Reporte;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface ReporteCassandraRepository extends CassandraRepository<Reporte, String> {
    @Query("SELECT * FROM Reporte WHERE tipo_ranking = 'GLOBAL' LIMIT 5")
    List<Reporte> obtenerTop5Global();
}
