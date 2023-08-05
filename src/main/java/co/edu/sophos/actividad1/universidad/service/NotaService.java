package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.model.Nota;
import co.edu.sophos.actividad1.universidad.repository.NotaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class NotaService {


    private final Logger LOGGER = LoggerFactory.getLogger(NotaService.class);
    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public Mono<Nota> findById(Integer id) {
        return notaRepository.findById(id)
                .onErrorResume(throwable -> {
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nota con id="+ id +" no encontrado").getMostSpecificCause()));
    }

    public Flux<Nota> findAll() {
        return notaRepository.findAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al consultar todas las notas", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ninguna nota encontrada").getMostSpecificCause()));
    }


    public Mono<Nota> save(Nota nota) {


        return notaRepository.save(nota)
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al crear una nota: " + nota, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "nota no creada").getMostSpecificCause()));

    }

    public Mono<Nota> update(int id, Nota nota) {
        return notaRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalNota -> {
                    if (optionalNota.isPresent()) {
                        nota.setId(id);
                        nota.setIdCurso(optionalNota.get().getIdCurso());
                        nota.setIdEstudiante(optionalNota.get().getIdEstudiante());
                        nota.setValor(optionalNota.get().getValor());

                        return notaRepository.save(nota)
                                .onErrorResume(throwable -> {
                                    LOGGER.error("Error al actualizar la nota: " + nota, throwable);
                                    return Mono.empty();
                                })
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "nota=" + nota +" no actualizada").getMostSpecificCause()));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Nota> deleteById(Integer id) {
        return notaRepository.findById(id)
                .flatMap(nota -> notaRepository.deleteById(nota.getId()).thenReturn(nota))
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar la nota con Id: " + id, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "nota con Id=" + id +" no borrado").getMostSpecificCause()));
    }
    public Mono<Void> deleteAll() {
        return notaRepository.deleteAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar todos las notas", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "notas no borradas").getMostSpecificCause()));
    }

}
