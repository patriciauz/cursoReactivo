package co.edu.sophos.actividad1.universidad.repository;

import co.edu.sophos.actividad1.universidad.model.Curso;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends R2dbcRepository<Curso, Integer> {
}
