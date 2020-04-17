package com.example.webflux.sandbox;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@RestController
public class SandboxController {

    @GetMapping("/text")
    public Mono<String> text() {
        return Mono.just("Hello, World!");
    }

    @GetMapping("/texts")
    public Flux<String> texts() {
        return Flux.just("Hello", "World!");
    }

    @GetMapping("/delayedtexts")
    public Flux<String> delayedTexts() {
        return Flux.just("Hello", "World!")
                .zipWith(Flux.interval(Duration.ofSeconds(1L)), (txt, c) -> txt);
    }

    @GetMapping("/message")
    public Mono<Message> message() {
        return Mono.just(new Message("Hello, World!"));
    }

    @GetMapping("/messages")
    public Flux<Message> messages() {
        return Flux.just(new Message("Hello"), new Message("World!"));
    }

    @GetMapping("/delayedmessages")
    public Flux<Message> delayedMessages() {
        return Flux.just(new Message("Hello"), new Message("World!"))
                .zipWith(Flux.interval(Duration.ofSeconds(1L)))
                .map(T -> T.getT1());
//                .zipWith(Flux.interval(Duration.ofSeconds(1L)), (msg, c) -> msg);
    }

    @PostMapping("/echo")
    public Mono<Message> echo(@RequestBody Mono<String> body) {
        return body.map(Message::new);
    }
}

