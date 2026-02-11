package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "estudiantes")
@Node("Estudiante")
public class Estudiante {
        @Id
        private String id;
        private String nombre;
        private String email;
        private Institucion institucion;
        private List<String> calificacionIds = new ArrayList<>();
    public Estudiante(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public void orElseThrow(Object estudianteNoEncontrado) {
        System.out.println("No existe estudiante");
    }
}

