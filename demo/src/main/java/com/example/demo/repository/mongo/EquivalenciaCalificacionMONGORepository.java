package com.example.demo.repository.mongo;

import com.example.demo.model.EquivalenciaCalificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquivalenciaCalificacionMONGORepository extends MongoRepository<EquivalenciaCalificacion, String> {
    List<EquivalenciaCalificacion> findAllByOrderByVigenciaDesdeDesc();
}
