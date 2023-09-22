package co.edu.sophos.actividad1.universidad.service;

import co.edu.sophos.actividad1.universidad.controller.DTO.UserDTO;
import co.edu.sophos.actividad1.universidad.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class UserService {


    JWTUtil JWTUtil;

    public UserService(JWTUtil JWTUtil) {
        this.JWTUtil = JWTUtil;
    }

    public Flux<User> userList(){
        List<User> list = new ArrayList<>();
        list.add(User.builder()
                .id(1)
                .username("patricia")
                .password("1234")
                .role("ADMIN")
                .build());
        list.add(User.builder()
                .id(2)
                .username("Zuleta")
                .password("0000")
                .role("USER")
                .build());
        return Flux.fromIterable(list);
    }

    public Mono<UserDetails> findUserByUsername(String username){
        return this.userList()
                .filter(user -> user.getUsername().equals(username))
                .next()
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build());
    }

    public Mono<UserDTO> generateToken(User user){
        return this.findUserByUsername(user.getUsername())
                .filter(userDetails -> userDetails.getPassword().equals(user.getPassword()))
                .switchIfEmpty(Mono.empty())
                .map(userDetails -> UserDTO.builder()
                        .username(userDetails.getUsername())
                        .token(JWTUtil.generateToken(userDetails))
                        .build());
    }
}
