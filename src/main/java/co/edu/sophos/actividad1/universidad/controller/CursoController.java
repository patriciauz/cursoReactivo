package co.edu.sophos.actividad1.universidad.controller;

import co.edu.sophos.actividad1.universidad.model.Curso;
import co.edu.sophos.actividad1.universidad.service.CursoKafkaConsumerService;
import co.edu.sophos.actividad1.universidad.service.CursoSQSService;
import co.edu.sophos.actividad1.universidad.service.CursoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/curso")
public class CursoController {

    CursoService cursoService;

    CursoKafkaConsumerService cursoKafkaConsumerService;
    CursoSQSService cursoSQSService;

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



    @GetMapping("/topico-kakfa/{topico}")
    public Mono<String> getCursoFromTopicoKafka(@PathVariable String topico) {
        return Mono.just(cursoKafkaConsumerService.obtenerUltimoCurso(topico));
    }

    @PostMapping("/aws/createQueue")
    public Mono<String> postCreateQueue(@RequestBody Map<String, Object> requestBody){
        return Mono.just(cursoSQSService.createStandardQueue((String) requestBody.get("queueName")));
    }

    @PostMapping("/aws/postMessageQueue/{queueName}")
    public Mono<String> postMessageQueue(@RequestBody Curso curso, @PathVariable String queueName){
        return Mono.just(cursoSQSService.publishStandardQueueMessage(
                queueName,
                2,
                curso));
    }

    @PostMapping("/aws/processCursoByNombre")
    public Mono<Curso> deleteCursoFromQueueByNombre(@RequestBody Map<String, Object> requestBody){
        return cursoSQSService.deleteCursoMessageInQueue((String) requestBody.get("queueName"),
                (Integer) requestBody.get("maxNumberMessages"),
                (Integer) requestBody.get("waitTimeSeconds"),
                (String) requestBody.get("descripcionCredito"));
    }


}
