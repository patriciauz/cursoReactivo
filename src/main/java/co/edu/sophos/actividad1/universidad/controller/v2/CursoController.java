package co.edu.sophos.actividad1.universidad.controller.v2;

import co.edu.sophos.actividad1.universidad.model.Curso;
import co.edu.sophos.actividad1.universidad.service.v2.CursoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequestMapping("v2/curso")
public class CursoController {

    CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/")
    public Flux<Curso> getAllCurso() {
        return cursoService.findAll();
    }


}
