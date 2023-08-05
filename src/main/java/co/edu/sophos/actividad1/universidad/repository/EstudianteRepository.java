package co.edu.sophos.actividad1.universidad.repository;

import co.edu.sophos.actividad1.universidad.model.Estudiante;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends R2dbcRepository<Estudiante, Integer> {


}
