package com.example.demo.repository.mongo;
import com.example.demo.model.Calificacion;
import com.example.demo.model.Institucion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionMONGORepository extends MongoRepository<Calificacion,String> {
    List<Calificacion> findByPaisOrigen(String pais);
    List<Calificacion> findByEstudianteIdOrderByFechaProcesamientoDesc(String estudianteId);

}
