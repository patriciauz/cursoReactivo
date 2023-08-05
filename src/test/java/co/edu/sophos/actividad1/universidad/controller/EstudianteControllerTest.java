package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Estudiante;
import co.edu.sophos.actividad1.universidad.service.EstudianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstudianteControllerTest {


    @Mock
    private EstudianteService estudianteService;

    @InjectMocks
    private EstudianteController estudianteController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetEstudianteByIdExitoso() throws Exception {
        int id=1;
        Estudiante estudianteEsperado = new Estudiante(id, 1216720506, "Estudiante test", 1);
        when(estudianteService.findById(any())).thenReturn(Mono.just(estudianteEsperado));
        Mono<Estudiante> resultado= estudianteController.getEstudianteById(id);
        assertEquals(estudianteEsperado, resultado.block());
    }


    @Test
    public void testGetEstudianteByIdNoEncontrado() {
        int id = 1;
        when(estudianteService.findById(id)).thenReturn(Mono.empty());
        when(estudianteService.findById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Estudiante not found"));
        assertThrows(ResponseStatusException.class, () -> estudianteController.getEstudianteById(id).block());
    }

    @Test
    void testGetAllEstudiantesExitoso() {
        Estudiante estudianteEsperado = new Estudiante(2, 1216720506, "Estudiante test", 1);
        Estudiante estudianteEsperado2 = new Estudiante(3, 1016725000, "Estudiante test", 2);
        Flux<Estudiante> expectedEstudiantes = Flux.just(estudianteEsperado, estudianteEsperado2);
        when(estudianteService.findAll()).thenReturn(expectedEstudiantes);
        Flux<Estudiante> resultado = estudianteController.getAllEstudiante();
        resultado.subscribe();
        assertEquals(expectedEstudiantes, resultado);
    }


    @Test
    void createEstudianteExitoso() {
        Estudiante estudianteEsperado  = new Estudiante(3, 1016725000, "Estudiante test", 2);
        when(estudianteService.save(any(Estudiante.class))).thenReturn(Mono.just(estudianteEsperado));
        Mono<Estudiante> result = estudianteController.createEstudiante(estudianteEsperado);
        StepVerifier.create(result)
                .expectNext(estudianteEsperado)
                .verifyComplete();
    }

    @Test
    public void testCreateEstudianteFallido() {
        Estudiante estudianteEsperado = new Estudiante(3, 1016725000, "Estudiante test", 2);

        when(estudianteService.save(any(Estudiante.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "estudiante no creado")));

        StepVerifier.create(estudianteController.createEstudiante(estudianteEsperado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testUpdateEstudianteExitoso() {
        Estudiante estudianteActualizado = new Estudiante(3, 1016725000, "Estudiante test", 2);
        when(estudianteService.update(any(Integer.class), any(Estudiante.class))).thenReturn(Mono.just(estudianteActualizado));
        Mono<Estudiante> result = estudianteController.updateEstudiante(estudianteActualizado.getId(), estudianteActualizado);
        StepVerifier.create(result)
                .expectNext(estudianteActualizado)
                .verifyComplete();
    }


    @Test
    public void testUpdateEstudianteFallido() {
        Estudiante estudianteActualizado = new Estudiante(3, 1016725000, "Estudiante test", 2);
        when(estudianteService.update(any(Integer.class), any(Estudiante.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no actualizado")));
        StepVerifier.create(estudianteController.updateEstudiante(estudianteActualizado.getId(), estudianteActualizado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testDeleteEstudianteByIdExitoso() throws Exception {
        int id = 6;
        Estudiante estudianteABorrar = new Estudiante(3, 1016725000, "Estudiante test", 2);
        when(estudianteService.deleteById(any())).thenReturn(Mono.just(estudianteABorrar));
        Mono<Estudiante> resultado = estudianteController.deleteEstudianteById(id);
        assertEquals(estudianteABorrar, resultado.block());
    }

    @Test
    public void testDeleteEstudianteByIdFallido() {
        int id = 1;
        when(estudianteService.deleteById(id)).thenReturn(Mono.empty());
        when(estudianteService.deleteById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Estudiante no borrado"));
        assertThrows(ResponseStatusException.class, () -> estudianteController.deleteEstudianteById(id).block());
    }

    @Test
    void testDeleteAllEstudiantesExitoso() {
        when(estudianteService.deleteAll()).thenReturn(Mono.empty());
        Mono<Void> resultado = estudianteController.deleteAllEstudiante();
        resultado.subscribe();
        assertEquals(Mono.empty(), resultado);
    }

}
