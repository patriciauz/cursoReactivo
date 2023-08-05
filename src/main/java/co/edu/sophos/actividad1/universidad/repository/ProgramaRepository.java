package co.edu.sophos.actividad1.universidad.repository;

import co.edu.sophos.actividad1.universidad.model.Estudiante;
import co.edu.sophos.actividad1.universidad.model.Programa;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaRepository extends R2dbcRepository<Programa, Integer> {
}
