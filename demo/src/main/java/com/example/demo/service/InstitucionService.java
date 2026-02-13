package com.example.demo.service;
import com.example.demo.model.Institucion;
import com.example.demo.repository.mongo.InstitucionMONGORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InstitucionService {
    @Autowired
    private InstitucionMONGORepository institucionRepository;


    public Institucion registrarInstitucion(String institucionId, Map<String, Object> metadatos) {    Institucion i = new Institucion();
            i.setId(institucionId);
            Institucion guardada = institucionRepository.save(i);
            System.out.println("El ID generado es: " + guardada.getId());
            return guardada;
        }
        }


