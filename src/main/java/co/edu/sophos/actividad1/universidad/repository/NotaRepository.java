package co.edu.sophos.actividad1.universidad.repository;

import co.edu.sophos.actividad1.universidad.model.Nota;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepository extends R2dbcRepository<Nota, Integer> {
}
