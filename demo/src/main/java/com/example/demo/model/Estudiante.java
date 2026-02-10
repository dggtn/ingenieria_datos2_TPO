package com.example.demo.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Document(collection = "estudiantes")
@Node("EStudiante")
public class Estudiante {
        @Id
        private String id;

        private String nombre;
        private String email;
        private Calificacion calificacion;
        private Trayectoria trayectoriaAcademica;
        private Institucion institucion;

    public Estudiante(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

}

