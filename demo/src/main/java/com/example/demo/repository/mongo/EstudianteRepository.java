package com.example.demo.repository.mongo;


import com.example.demo.model.Estudiante;



public interface EstudianteRepository {

    Estudiante save(Estudiante estudiante);

     Estudiante findById(String id);
}
