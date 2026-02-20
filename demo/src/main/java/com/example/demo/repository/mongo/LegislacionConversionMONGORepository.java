package com.example.demo.repository.mongo;

import com.example.demo.model.LegislacionConversion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegislacionConversionMONGORepository extends MongoRepository<LegislacionConversion, String> {
}
