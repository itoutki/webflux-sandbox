package com.example.webflux.sandbox.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class SecurityController {

    @GetMapping("/security/public")
    public Mono<String> pub() {
        return Mono.just("public");
    }

    @GetMapping("/security/private")
    public Mono<String> priv() {
        return Mono.just("private");
    }
}
