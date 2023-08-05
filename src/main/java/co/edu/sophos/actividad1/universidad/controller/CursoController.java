package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Curso;
import co.edu.sophos.actividad1.universidad.service.CursoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/curso")
public class CursoController {

    CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/{id}")
    public Mono<Curso> getCursoById(@PathVariable Integer id) {
        return cursoService.findById(id);
    }


    @GetMapping("/")
    public Flux<Curso> getAllCurso() {
        return cursoService.findAll();
    }

    @PostMapping("/")
    public Mono<Curso> createCurso(@RequestBody Curso curso){
        return cursoService.save(curso);
    }

    @PutMapping("/{id}")
    public Mono<Curso> updateCurso(@PathVariable Integer id, @RequestBody Curso curso){
        return cursoService.update(id, curso);
    }

    @DeleteMapping("/{id}")
    public Mono<Curso> deleteCursoById(@PathVariable Integer id){
        return cursoService.deleteById(id);
    }

    @DeleteMapping("/")
    public Mono<Void> deleteAllCurso(){
        return cursoService.deleteAll();
    }


}
