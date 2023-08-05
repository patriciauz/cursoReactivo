package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Nota;
import co.edu.sophos.actividad1.universidad.model.Nota;
import co.edu.sophos.actividad1.universidad.service.NotaService;
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
public class NotaControllerTest {


    @Mock
    private NotaService notaService;

    @InjectMocks
    private NotaController notaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetNotaByIdExitodo() throws Exception {
        int id=1;
        Nota notaEsperado = new Nota(id, 5,6,6);
        when(notaService.findById(any())).thenReturn(Mono.just(notaEsperado));
        Mono<Nota> resultado= notaController.getNotaById(id);
        assertEquals(notaEsperado, resultado.block());
    }


    @Test
    public void testGetNotaByIdNoEncontrado() {
        int id = 1;
        when(notaService.findById(id)).thenReturn(Mono.empty());
        when(notaService.findById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Nota not found"));
        assertThrows(ResponseStatusException.class, () -> notaController.getNotaById(id).block());
    }

    @Test
    void testGetAllNotasExitoso() {
        Nota notaEsperado = new Nota(5, 5,6,6);
        Nota notaEsperado2 = new Nota(4, 6,6,6);
        Flux<Nota> expectedNotas = Flux.just(notaEsperado, notaEsperado2);
        when(notaService.findAll()).thenReturn(expectedNotas);
        Flux<Nota> resultado = notaController.getAllNota();
        resultado.subscribe();
        assertEquals(expectedNotas, resultado);
    }


    @Test
    void createNotaExitoso() {
        Nota notaEsperado = new Nota(3, 1,3,6);
        when(notaService.save(any(Nota.class))).thenReturn(Mono.just(notaEsperado));
        Mono<Nota> result = notaController.createNota(notaEsperado);
        StepVerifier.create(result)
                .expectNext(notaEsperado)
                .verifyComplete();
    }

    @Test
    public void testCreateNotaFallido() {
        Nota notaEsperado = new Nota(4, 3,4,4);

        when(notaService.save(any(Nota.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no creada")));

        StepVerifier.create(notaController.createNota(notaEsperado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testUpdateNotaExitoso() {
        Nota notaActualizado = new Nota(6, 4,7,8);
        when(notaService.update(any(Integer.class), any(Nota.class))).thenReturn(Mono.just(notaActualizado));
        Mono<Nota> result = notaController.updateNota(notaActualizado.getId(), notaActualizado);
        StepVerifier.create(result)
                .expectNext(notaActualizado)
                .verifyComplete();
    }


    @Test
    public void testUpdateNotaFallido() {
        Nota notaActualizado = new Nota(7, 2,7,8);
        when(notaService.update(any(Integer.class), any(Nota.class))).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no actualizada")));
        StepVerifier.create(notaController.updateNota(notaActualizado.getId(), notaActualizado))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testDeleteNotaByIdExitoso() throws Exception {
        int id = 6;
        Nota notaABorrar = new Nota(id, 5,7,6);
        when(notaService.deleteById(any())).thenReturn(Mono.just(notaABorrar));
        Mono<Nota> resultado = notaController.deleteNotaById(id);
        assertEquals(notaABorrar, resultado.block());
    }

    @Test
    public void testDeleteNotaByIdFallido() {
        int id = 1;
        when(notaService.deleteById(id)).thenReturn(Mono.empty());
        when(notaService.deleteById(id)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Nota no borrada"));
        assertThrows(ResponseStatusException.class, () -> notaController.deleteNotaById(id).block());
    }

    @Test
    void testDeleteAllNotasExitoso() {
        when(notaService.deleteAll()).thenReturn(Mono.empty());
        Mono<Void> resultado = notaController.deleteAllNota();
        resultado.subscribe();
        assertEquals(Mono.empty(), resultado);
    }


}
