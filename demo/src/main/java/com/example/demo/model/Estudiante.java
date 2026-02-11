package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "estudiantes")
public class Estudiante {
    @Id
    private String id;
    private String nombre;
    private String paisOrigen;
    private List<String> calificacionIds = new ArrayList<>();
}

