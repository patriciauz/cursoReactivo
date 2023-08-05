package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Nota;
import co.edu.sophos.actividad1.universidad.service.NotaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/nota")
public class NotaController {

    NotaService notaService;


    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @GetMapping("/{id}")
    public Mono<Nota> getNotaById(@PathVariable Integer id) {
        return notaService.findById(id);
    }


    @GetMapping("/")
    public Flux<Nota> getAllNota() {
        return notaService.findAll();
    }

    @PostMapping("/")
    public Mono<Nota> createNota(@RequestBody Nota nota){
        return notaService.save(nota);
    }

    @PutMapping("/{id}")
    public Mono<Nota> updateNota(@PathVariable Integer id, @RequestBody Nota nota){
        return notaService.update(id, nota);
    }

    @DeleteMapping("/{id}")
    public Mono<Nota> deleteNotaById(@PathVariable Integer id){
        return notaService.deleteById(id);
    }

    @DeleteMapping("/")
    public Mono<Void> deleteAllNota(){
        return notaService.deleteAll();
    }

}
