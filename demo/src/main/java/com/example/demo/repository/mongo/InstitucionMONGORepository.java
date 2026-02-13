package com.example.demo.repository.mongo;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Institucion;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitucionMONGORepository extends MongoRepository<Institucion, String> {
}
