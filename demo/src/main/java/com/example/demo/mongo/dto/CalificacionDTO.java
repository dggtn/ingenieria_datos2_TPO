package com.example.demo.mongo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.query.sqm.tree.expression.Conversion;

import java.util.List;
import java.util.Map;

public class CalificacionDTO {
    private String id;
    @Schema(example = "A*", description = "Formato original según sistema de origen")
    private String valorOriginal;
    private String estudianteId;
    @Schema(example = "UK", description = "País del sistema educativo")
    private String sistemaOrigen; //si es de uk,alemania,etc
    private Map<String, Object> couseworsExams;
    private List<Conversion> conversiones;


}
