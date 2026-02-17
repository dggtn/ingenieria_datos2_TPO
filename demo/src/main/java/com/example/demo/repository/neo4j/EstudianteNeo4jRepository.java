package com.example.demo.repository.neo4j;
import com.example.demo.model.Estudiante;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EstudianteNeo4jRepository extends Neo4jRepository<Estudiante, UUID> {

    @Query("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (e)-[r:ESTUDIO_EN]->(i)\n" +
            "SET r.periodo = $periodo")
    void registrarDondeEstudio(UUID idEstudiante, UUID idInstitucion, String periodo);

    @Query ("MATCH (e:Estudiante {id: $idEstudiante})\n" +
            "WITH e\n" +
            "MATCH (m:Materia {id: $idMateria})\n" +
            "MERGE (e)-[r:CURSO]->(m)\n" +
            "SET r.periodo = $periodo,r.nivel = $nivel,r.resultado=$resultado")
    void registrarCursada(UUID idEstudiante,UUID idMateria,Double resultado,String periodo,String nivel);
    @Query ( "MATCH (m:Materia {id: $idMateria})\n" +
            "WITH m\n" +
            "MATCH (i:Institucion {id: $idInstitucion})\n" +
            "MERGE (m)-[r:SE_DICTA_EN]->(i)\n" +
            "SET r.nivel = $nivel")
    void registrarDondeDictaMateria(UUID idInstitucion,UUID idMateria,String periodo,String nivel);

}

