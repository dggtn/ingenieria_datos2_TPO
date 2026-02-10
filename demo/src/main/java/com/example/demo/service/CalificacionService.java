package com.example.demo.service;


import com.example.demo.model.Calificacion;
import com.example.demo.repository.mongo.CalificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalificacionService {
    @Autowired
    private  CalificacionRepository calificacionRepository;

   public Calificacion regstrarCalificacion (String nota, String tipoPais){
       Calificacion calificacion = new Calificacion();
       calificacion.setTipoPais(tipoPais);
       calificacion.setValorNota(nota);
       return calificacionRepository.save(calificacion);
   }




}
