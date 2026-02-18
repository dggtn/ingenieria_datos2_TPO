package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "instituciones")
@Node("Institucion")
public class Institucion {
    @org.springframework.data.annotation.Id
    @org.springframework.data.neo4j.core.schema.Id
    private String id;

    private String nombre;
    private String pais;
    private String region;
    private String provincia;
    private List<MateriaInstitucion> curriculum = new ArrayList<>();
    private Map<String, Object> metadatos;

    public Institucion() {
    }

    public Institucion(String id, String nombre, String pais, String region, String provincia, List<MateriaInstitucion> curriculum, Map<String, Object> metadatos) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.region = region;
        this.provincia = provincia;
        this.curriculum = curriculum != null ? curriculum : new ArrayList<>();
        this.metadatos = metadatos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<MateriaInstitucion> getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(List<MateriaInstitucion> curriculum) {
        this.curriculum = curriculum != null ? curriculum : new ArrayList<>();
    }

    public Map<String, Object> getMetadatos() {
        return metadatos;
    }

    public void setMetadatos(Map<String, Object> metadatos) {
        this.metadatos = metadatos;
    }

    public static class MateriaInstitucion {
        private String id;
        private String nombre;
        private Integer anioCarrera;

        public MateriaInstitucion() {
        }

        public MateriaInstitucion(String id, String nombre, Integer anioCarrera) {
            this.id = id;
            this.nombre = nombre;
            this.anioCarrera = anioCarrera;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Integer getAnioCarrera() {
            return anioCarrera;
        }

        public void setAnioCarrera(Integer anioCarrera) {
            this.anioCarrera = anioCarrera;
        }
    }
}
