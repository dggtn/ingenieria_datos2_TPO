package com.example.demo.service;
import com.example.demo.model.Institucion;
import com.example.demo.model.RequestRegistrarInstitucion;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InstitucionService {
    @Autowired
    private InstitucionMONGORepository institucionRepository;


    public Institucion registrarInstitucion(RequestRegistrarInstitucion requestRegistrarInstitucion) {
            Institucion i = new Institucion();
            i.setPais(requestRegistrarInstitucion.getPais());
            i.setNombre(requestRegistrarInstitucion.getNombre());
            Institucion guardada = institucionRepository.save(i);
            System.out.println("El ID generado es: " + guardada.getId());
            return guardada;
        }
        }


