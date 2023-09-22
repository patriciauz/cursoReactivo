package co.edu.sophos.actividad1.universidad.controller;


import co.edu.sophos.actividad1.universidad.controller.DTO.UserDTO;
import co.edu.sophos.actividad1.universidad.model.User;
import co.edu.sophos.actividad1.universidad.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<UserDTO>> createOrdenLocal(@RequestBody User user){
        return userService.generateToken(user)
                .map(userResponseDTO -> new ResponseEntity<>(userResponseDTO, HttpStatus.OK))
                .switchIfEmpty( Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
