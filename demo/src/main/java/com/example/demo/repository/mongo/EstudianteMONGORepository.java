package com.example.demo.repository.mongo;
import com.example.demo.model.Estudiante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteMONGORepository extends MongoRepository<Estudiante, String> {
}
