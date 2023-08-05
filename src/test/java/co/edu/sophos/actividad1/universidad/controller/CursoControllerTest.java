package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Curso;
import co.edu.sophos.actividad1.universidad.service.CursoService;
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
 class CursoControllerTest {

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCursoByIdExitoso() throws Exception {
        int id=1;
        Curso cursoEsperado = new Curso(id,"Curso test");
        when(cursoService.findById(any())).thenReturn(Mono.just(cursoEsperado));
        Mono<Curso> resultado= cursoController.getCursoById(id);
        assertEquals(cursoEsperado, resultado.block());
    }


    @Test
    public void testGetCursoByIdNoEncontrado() {
        int id = 1;
        when(cursoService.findById(id)).thenReturn(Mono.empty());
        when(cursoService.findById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Curso not found"));
        assertThrows(ResponseStatusException.class, () -> cursoController.getCursoById(id).block());
    }

    @Test
    void testGetAllCursosExitoso() {
        Curso cursoEsperado = new Curso(2, "Curso test");
        Curso cursoEsperado2 = new Curso(3,"Curso test");
        Flux<Curso> expectedCursos = Flux.just(cursoEsperado, cursoEsperado2);
        when(cursoService.findAll()).thenReturn(expectedCursos);
        Flux<Curso> resultado = cursoController.getAllCurso();
        resultado.subscribe();
        assertEquals(expectedCursos, resultado);
    }


    @Test
    void createCursoExitoso() {
        Curso cursoEsperado  = new Curso(3,  "Curso test");
        when(cursoService.save(any(Curso.class))).thenReturn(Mono.just(cursoEsperado));
        Mono<Curso> result = cursoController.createCurso(cursoEsperado);
        StepVerifier.create(result)
                .expectNext(cursoEsperado)
                .verifyComplete();
    }

    @Test
    public void testCreateCursoFallido() {
        Curso cursoEsperado = new Curso(3, "Curso test");
        when(cursoService.save(any(Curso.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no creado")));
        StepVerifier.create(cursoController.createCurso(cursoEsperado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testUpdateCursoExitoso() {
        Curso cursoActualizado = new Curso(3,"Curso test");
        when(cursoService.update(any(Integer.class), any(Curso.class))).thenReturn(Mono.just(cursoActualizado));
        Mono<Curso> result = cursoController.updateCurso(cursoActualizado.getId(), cursoActualizado);
        StepVerifier.create(result)
                .expectNext(cursoActualizado)
                .verifyComplete();
    }


    @Test
    public void testUpdateCursoFallido() {
        Curso cursoActualizado = new Curso(3, "Curso test");
        when(cursoService.update(any(Integer.class), any(Curso.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no actualizado")));
        StepVerifier.create(cursoController.updateCurso(cursoActualizado.getId(), cursoActualizado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testDeleteCursoByIdExitoso() throws Exception {
        int id = 6;
        Curso cursoABorrar = new Curso(3, "Curso test");
        when(cursoService.deleteById(any())).thenReturn(Mono.just(cursoABorrar));
        Mono<Curso> resultado = cursoController.deleteCursoById(id);
        assertEquals(cursoABorrar, resultado.block());
    }

    @Test
    public void testDeleteCursoByIdFallido() {
        int id = 1;
        when(cursoService.deleteById(id)).thenReturn(Mono.empty());
        when(cursoService.deleteById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Curso no borrado"));
        assertThrows(ResponseStatusException.class, () -> cursoController.deleteCursoById(id).block());
    }

    @Test
    void testDeleteAllCursosExitoso() {
        when(cursoService.deleteAll()).thenReturn(Mono.empty());
        Mono<Void> resultado = cursoController.deleteAllCurso();
        resultado.subscribe();
        assertEquals(Mono.empty(), resultado);
    }
}
