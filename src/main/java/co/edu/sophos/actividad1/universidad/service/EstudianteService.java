package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.model.Estudiante;
import co.edu.sophos.actividad1.universidad.model.Programa;
import co.edu.sophos.actividad1.universidad.repository.EstudianteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class EstudianteService {

    private final Logger LOGGER = LoggerFactory.getLogger(EstudianteService.class);
    private final EstudianteRepository estudianteRepository;
    private final ProgramaService programaService;

    public EstudianteService(EstudianteRepository estudianteRepository, ProgramaService programaService) {
        this.estudianteRepository = estudianteRepository;
        this.programaService = programaService;
    }

    public Mono<Estudiante> findById(Integer id) {
        return estudianteRepository.findById(id)
                .onErrorResume(throwable -> {
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Estudiante con id="+ id +" no encontrado").getMostSpecificCause()));
    }

    public Flux<Estudiante> findAll() {
        return estudianteRepository.findAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al consultar todos los estudiantes", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ningún estudiante encontrado").getMostSpecificCause()));
    }


    public Mono<Estudiante> save(Estudiante estudiante) {


        Mono<Programa> programa = validatePrograma(estudiante);

        return programa
                .flatMap(programaEncontrado -> estudianteRepository.save(estudiante)
                        .onErrorResume(throwable -> {
                            LOGGER.error("Error al crear un estudiante: " + estudiante, throwable);
                            return Mono.empty();
                        })
                )
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Programa no encontrado, estudiante no creado").getMostSpecificCause()));

    }

    public Mono<Estudiante> update(int id, Estudiante estudiante) {
        return estudianteRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalEstudiante -> {
                    if (optionalEstudiante.isPresent()) {
                        estudiante.setId(id);
                        estudiante.setNombreCompleto(optionalEstudiante.get().getNombreCompleto());
                        estudiante.setIdentificacion(optionalEstudiante.get().getIdentificacion());
                        estudiante.setIdPrograma(optionalEstudiante.get().getIdPrograma());

                        return estudianteRepository.save(estudiante)
                                .onErrorResume(throwable -> {
                                    LOGGER.error("Error al actualizar el estudiante: " + estudiante, throwable);
                                    return Mono.empty();
                                })
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "estudiante=" + estudiante +" no actualizado").getMostSpecificCause()));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Estudiante> deleteById(Integer id) {
        return estudianteRepository.findById(id)
                .flatMap(estudiante -> estudianteRepository.deleteById(estudiante.getId()).thenReturn(estudiante))
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar el estudiante con Id: " + id, throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "estudiante con Id=" + id +" no borrado").getMostSpecificCause()));
    }
    public Mono<Void> deleteAll() {
        return estudianteRepository.deleteAll()
                .onErrorResume(throwable -> {
                    LOGGER.error("Error al borrar todos los estudiantes", throwable);
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "estudiantes no borrados").getMostSpecificCause()));
    }

    private Mono<Programa> validatePrograma(Estudiante estudiante){

       return programaService.findById(estudiante.getIdPrograma())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Programa no encontrado").getMostSpecificCause()));
    }

}
