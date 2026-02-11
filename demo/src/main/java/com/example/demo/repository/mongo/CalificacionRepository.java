package com.example.demo.repository.mongo;

import com.example.demo.model.Calificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends MongoRepository<Calificacion, String> {
    List<Calificacion> findByEstudianteId(String estudianteId);
}
