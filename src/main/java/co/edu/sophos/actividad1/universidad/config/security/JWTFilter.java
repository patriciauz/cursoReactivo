package co.edu.sophos.actividad1.universidad.config.security;

import co.edu.sophos.actividad1.universidad.service.JWTUtil;
import co.edu.sophos.actividad1.universidad.service.UserService;
import io.micrometer.common.lang.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import java.nio.charset.StandardCharsets;

@Component
public class JWTFilter implements WebFilter {
    UserService userService;
    JWTUtil JWTUtil;

    public JWTFilter(UserService userService, JWTUtil utilJWT) {
        this.userService = userService;
        this.JWTUtil = utilJWT;
    }

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return validateHeader(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .map(jwt -> JWTUtil.validateTokenAndRetrieveSubject(jwt))
                .flatMap(user -> userService.findUserByUsername(user.getUsername()))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()))
                .flatMap(authentication -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)))
                .switchIfEmpty(chain.filter(exchange))
                .onErrorResume(err -> handleError(exchange, err));
    }

    private Mono<String> validateHeader(String header){
        if(header != null && !header.isBlank() && header.startsWith("Bearer ")){
            String jwt = header.substring(7);
            if(jwt == null || jwt.isBlank()) return Mono.empty();
            else return Mono.just(jwt);
        }
        return Mono.empty();
    }

    private Mono<Void> handleError(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setRawStatusCode(HttpStatus.UNAUTHORIZED.value());
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        return exchange
                .getResponse()
                .writeWith(
                        Flux.just(
                                exchange.getResponse().bufferFactory().wrap(ex.getMessage().getBytes(StandardCharsets.UTF_8))));
    }
}
