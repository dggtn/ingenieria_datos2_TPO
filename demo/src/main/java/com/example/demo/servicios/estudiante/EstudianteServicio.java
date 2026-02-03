package com.example.demo.servicios.estudiante;
import ar.com.uade.tiendaOnline.tpo.entidad.Categoria;
import ar.com.uade.tiendaOnline.tpo.entidad.dto.CategoriaDTO;
import ar.com.uade.tiendaOnline.tpo.excepciones.CategoriaDuplicateExcepcion;
import ar.com.uade.tiendaOnline.tpo.excepciones.CategoriaInexistenteExcepcion;
import ar.com.uade.tiendaOnline.tpo.repositorio.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteServicio implements IEstudianteServicio {

}