package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Document(collection = "usuarios")
@Node("Usuario")
public class Usuario {
        @Id
        private String id;

        private String username;
        private String email;
    }

