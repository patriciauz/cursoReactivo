package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Estudiante;
import co.edu.sophos.actividad1.universidad.service.EstudianteService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/estudiante")
public class EstudianteController {

    EstudianteService estudianteService;


    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/{id}")
    public Mono<Estudiante> getEstudianteById(@PathVariable Integer id) {
        return estudianteService.findById(id);
    }


    @GetMapping("/")
    public Flux<Estudiante> getAllEstudiante() {
        return estudianteService.findAll();
    }

    @PostMapping("/")
    public Mono<Estudiante> createEstudiante(@RequestBody Estudiante estudiante){
        return estudianteService.save(estudiante);
    }

    @PutMapping("/{id}")
    public Mono<Estudiante> updateEstudiante(@PathVariable Integer id, @RequestBody Estudiante estudiante){
        return estudianteService.update(id, estudiante);
    }

    @DeleteMapping("/{id}")
    public Mono<Estudiante> deleteEstudianteById(@PathVariable Integer id){
        return estudianteService.deleteById(id);
    }

    @DeleteMapping("/")
    public Mono<Void> deleteAllEstudiante(){
        return estudianteService.deleteAll();
    }



}
