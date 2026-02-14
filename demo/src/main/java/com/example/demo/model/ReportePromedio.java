package com.example.demo.model;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table
public class ReportePromedio {

    private double promedio;
    private LocalDate fecha;
    @PrimaryKey
    private UUID id;

    public ReportePromedio( double promedio, LocalDate fecha) {
        this.promedio = promedio;
        this.fecha = fecha;
    }

    public ReportePromedio() {
        this.id = Uuids.timeBased();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
