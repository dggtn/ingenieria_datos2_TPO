package com.example.demo.cassandra.servicios;


import ar.com.uade.tiendaOnline.tpo.entidad.Categoria;
import ar.com.uade.tiendaOnline.tpo.entidad.dto.CategoriaDTO;
import ar.com.uade.tiendaOnline.tpo.excepciones.CategoriaDuplicateExcepcion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Optional;
@Service
public interface IEstudianteServicio {

    ResponseEntity<Estudiante> getEstudiantes();

    Optional<Estudiante> getEstudianteById(Long estudianteId);

    Estudiante crearEstudiante(Object o);
}

