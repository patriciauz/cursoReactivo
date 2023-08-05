package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.model.Curso;
import co.edu.sophos.actividad1.universidad.repository.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CursoService {

    private final Logger LOGGER = LoggerFactory.getLogger(CursoService.class);
    private final CursoRepository cursoRepository;
    
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Mono<Curso> findById(Integer id) {
        return cursoRepository.findById(id)
                .onErrorResume(throwable -> {
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Curso con id="+ id +" no encontrado").getMostSpecificCause()));
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


    public Mono<Curso> save(Curso curso) {


        return cursoRepository.save(curso)
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al crear un Curso: " + curso, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Credito no creado").getMostSpecificCause()));

    }

    public Mono<Curso> update(int id, Curso curso) {
        return cursoRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalCurso -> {
                    if (optionalCurso.isPresent()) {
                        curso.setId(id);
                        curso.setNombre(optionalCurso.get().getNombre());

                        return cursoRepository.save(curso)
                                .onErrorResume(throwable -> {
                                    LOGGER.error("Error al actualizar el Curso: " + curso, throwable);
                                    return Mono.empty();
                                })
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "estudiante=" + curso +" no actualizado").getMostSpecificCause()));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Curso> deleteById(Integer id) {
        return cursoRepository.findById(id)
                .flatMap(curso -> cursoRepository.deleteById(curso.getId()).thenReturn(curso))
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar el Curso con Id: " + id, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Curso con Id=" + id +" no borrado").getMostSpecificCause()));
    }
    public Mono<Void> deleteAll() {
        return cursoRepository.deleteAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar todos los Cursos", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cursos no borrados").getMostSpecificCause()));
    }


}
