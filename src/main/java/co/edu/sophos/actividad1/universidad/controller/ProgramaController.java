package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Programa;
import co.edu.sophos.actividad1.universidad.service.ProgramaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/programa")
public class ProgramaController {

    ProgramaService programaService;

    public ProgramaController(ProgramaService programaService) {
        this.programaService = programaService;
    }

    @GetMapping("/{id}")
    public Mono<Programa> getProgramaById(@PathVariable Integer id) {
        return programaService.findById(id);
    }


    @GetMapping("/")
    public Flux<Programa> getAllPrograma() {
        return programaService.findAll();
    }

    @PostMapping("/")
    public Mono<Programa> createPrograma(@RequestBody Programa programa){
        return programaService.save(programa);
    }

    @PutMapping("/{id}")
    public Mono<Programa> updatePrograma(@PathVariable Integer id, @RequestBody Programa programa){
        return programaService.update(id, programa);
    }

    @DeleteMapping("/{id}")
    public Mono<Programa> deleteProgramaById(@PathVariable Integer id){
        return programaService.deleteById(id);
    }

    @DeleteMapping("/")
    public Mono<Void> deleteAllPrograma(){
        return programaService.deleteAll();
    }




}
