package com.example.demo.repository.cassandra;
import com.example.demo.model.ReportePromedio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface ReporteCassandraRepository extends CrudRepository<ReportePromedio, String> {

}
