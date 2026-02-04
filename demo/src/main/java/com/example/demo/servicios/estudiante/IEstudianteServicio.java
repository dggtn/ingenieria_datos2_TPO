package com.example.demo.servicios.estudiante;


import ar.com.uade.tiendaOnline.tpo.entidad.Categoria;
import ar.com.uade.tiendaOnline.tpo.entidad.dto.CategoriaDTO;
import ar.com.uade.tiendaOnline.tpo.excepciones.CategoriaDuplicateExcepcion;
import com.example.demo.entidad.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Optional;
@Service
public interface IEstudianteServicio {

    ResponseEntity<Estudiante> getEstudiantes();

    Optional<Estudiante> getEstudianteById(Long estudianteId);

    Estudiante crearEstudiante(Object o);
}

