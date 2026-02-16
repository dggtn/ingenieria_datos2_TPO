package com.example.demo.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Reporte {
    @PrimaryKeyColumn(name = "tipo", type = PrimaryKeyType.PARTITIONED)
    private String tipo; // "ESTUDIANTE", "PAIS", "INSTITUTO"

    @PrimaryKeyColumn(name = "promedio", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Double promedio;

    @PrimaryKeyColumn(name = "nombre", type = PrimaryKeyType.CLUSTERED)
    private String nombre;

    public Reporte(String tipo, Double promedio, String nombre) {
        this.tipo = tipo;
        this.promedio = promedio;
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
