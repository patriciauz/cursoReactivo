package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Programa;
import co.edu.sophos.actividad1.universidad.service.ProgramaService;
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
class ProgramaControllerTest {


    @Mock
    private ProgramaService programaService;

    @InjectMocks
    private ProgramaController programaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetProgramaByIdExitodo() throws Exception {
        int id=1;
        Programa programaEsperado = new Programa(id, "Programa test");
        when(programaService.findById(any())).thenReturn(Mono.just(programaEsperado));
        Mono<Programa> resultado= programaController.getProgramaById(id);
        assertEquals(programaEsperado, resultado.block());
    }


    @Test
    public void testGetProgramaByIdNoEncontrado() {
        int id = 1;
        when(programaService.findById(id)).thenReturn(Mono.empty());
        when(programaService.findById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Programa not found"));
        assertThrows(ResponseStatusException.class, () -> programaController.getProgramaById(id).block());
    }

    @Test
    void testGetAllProgramasExitoso() {
        Programa programaEsperado = new Programa(5, "Test Programa");
        Programa programaEsperado2 = new Programa(4, "Test Programa 2");
        Flux<Programa> expectedProgramas = Flux.just(programaEsperado, programaEsperado2);
        when(programaService.findAll()).thenReturn(expectedProgramas);
        Flux<Programa> resultado = programaController.getAllPrograma();
        resultado.subscribe();
        assertEquals(expectedProgramas, resultado);
    }


    @Test
    void createProgramaExitoso() {
        Programa programaEsperado = new Programa(3, "Programa test");
        when(programaService.save(any(Programa.class))).thenReturn(Mono.just(programaEsperado));
        Mono<Programa> result = programaController.createPrograma(programaEsperado);
        StepVerifier.create(result)
                .expectNext(programaEsperado)
                .verifyComplete();
    }

    @Test
    public void testCreateProgramaFallido() {
        Programa programaEsperado = new Programa(4, "Programa test");

        when(programaService.save(any(Programa.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "programa no creado")));

        StepVerifier.create(programaController.createPrograma(programaEsperado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testUpdateProgramaExitoso() {
        Programa programaActualizado = new Programa(6, "Test Programa");
        when(programaService.update(any(Integer.class), any(Programa.class))).thenReturn(Mono.just(programaActualizado));
        Mono<Programa> result = programaController.updatePrograma(programaActualizado.getId(), programaActualizado);
        StepVerifier.create(result)
                .expectNext(programaActualizado)
                .verifyComplete();
    }


    @Test
    public void testUpdateProgramaFallido() {
        Programa programaActualizado = new Programa(7, "Test Programa");

            when(programaService.update(any(Integer.class), any(Programa.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Programa no actualizado")));

        StepVerifier.create(programaController.updatePrograma(programaActualizado.getId(), programaActualizado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testDeleteProgramaByIdExitoso() throws Exception {
        int id = 6;
        Programa programaABorrar = new Programa(id, "Test programa");
        when(programaService.deleteById(any())).thenReturn(Mono.just(programaABorrar));
        Mono<Programa> resultado = programaController.deleteProgramaById(id);
        assertEquals(programaABorrar, resultado.block());
    }

    @Test
    public void testDeleteProgramaByIdFallido() {
        int id = 1;
        when(programaService.deleteById(id)).thenReturn(Mono.empty());
        when(programaService.deleteById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Programa no borrado"));
        assertThrows(ResponseStatusException.class, () -> programaController.deleteProgramaById(id).block());
    }

    @Test
    void testDeleteAllProgramasExitoso() {
        when(programaService.deleteAll()).thenReturn(Mono.empty());
        Mono<Void> resultado = programaController.deleteAllPrograma();
        resultado.subscribe();
        assertEquals(Mono.empty(), resultado);
    }

}
