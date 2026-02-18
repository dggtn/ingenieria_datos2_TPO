package com.example.demo.repository.mongo;
import com.example.demo.model.Calificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionMONGORepository extends MongoRepository<Calificacion,String> {
}
