package com.example.demo.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Reporte {
    @PrimaryKeyColumn(name = "tipo_ranking", type = PrimaryKeyType.PARTITIONED)
    private String tipoRanking = "GLOBAL";
    private String estudianteid;
    private Double promedio;

    public Reporte(String estudianteid, Double promedio) {
        this.estudianteid = estudianteid;
        this.promedio = promedio;
    }

    public String getEstudianteid() {
        return estudianteid;
    }

    public void setEstudianteid(String estudianteid) {
        this.estudianteid = estudianteid;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }
}
