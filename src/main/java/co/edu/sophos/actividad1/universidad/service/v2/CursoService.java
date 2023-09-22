package co.edu.sophos.actividad1.universidad.service.v2;

import co.edu.sophos.actividad1.universidad.model.Curso;
import co.edu.sophos.actividad1.universidad.repository.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CursoService {

    private final Logger LOGGER = LoggerFactory.getLogger(co.edu.sophos.actividad1.universidad.service.CursoService.class);
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Flux<Curso> findAll() {
        return cursoRepository.findAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al consultar todos los Cursos", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ningún Curso encontrado").getMostSpecificCause()));
    }

}
