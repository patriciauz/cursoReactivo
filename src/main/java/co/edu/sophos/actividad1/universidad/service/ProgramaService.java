package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.model.Programa;
import co.edu.sophos.actividad1.universidad.repository.ProgramaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ProgramaService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProgramaService.class);
    private final ProgramaRepository programaRepository;

    public ProgramaService(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    public Mono<Programa> findById(Integer id) {
        return programaRepository.findById(id)
                .onErrorResume(throwable -> {
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Programa con id="+ id +" no encontrado").getMostSpecificCause()));
    }

    public Flux<Programa> findAll() {
        return programaRepository.findAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al consultar todos los programas", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ningún programa encontrado").getMostSpecificCause()));
    }


    public Mono<Programa> save(Programa programa) {


        return programaRepository.save(programa)
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al crear un programa: " + programa, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Credito no creado").getMostSpecificCause()));

    }

    public Mono<Programa> update(int id, Programa programa) {
        return programaRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalPrograma -> {
                    if (optionalPrograma.isPresent()) {
                        programa.setId(id);
                        programa.setNombre(optionalPrograma.get().getNombre());


                        return programaRepository.save(programa)
                                .onErrorResume(throwable -> {
                                    LOGGER.error("Error al actualizar el programa: " + programa, throwable);
                                    return Mono.empty();
                                })
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "programa=" + programa +" no actualizado").getMostSpecificCause()));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Programa> deleteById(Integer id) {
        return programaRepository.findById(id)
                .flatMap(programa -> programaRepository.deleteById(programa.getId()).thenReturn(programa))
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar el programa con Id: " + id, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "programa con Id=" + id +" no borrado").getMostSpecificCause()));
    }
    public Mono<Void> deleteAll() {
        return programaRepository.deleteAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar todos los programas", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "programas no borrados").getMostSpecificCause()));
    }

}
