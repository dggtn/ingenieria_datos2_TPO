package com.example.demo.repository.cassandra;

import com.example.demo.model.CalificacionCassandra;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionCassandraRepository extends CassandraRepository<CalificacionCassandra, String> {
    @Query("SELECT institucionid, institucionnombre, institucionpais, institucionprovincia, institucionniveleducativo, estudianteid, notaconversionsudafrica FROM calificaciones")
    List<CalificacionCassandra> obtenerDatosParaRankingInstituciones();
}
