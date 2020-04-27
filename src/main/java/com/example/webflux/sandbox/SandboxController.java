package com.example.webflux.sandbox;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class SandboxController {

    @ModelAttribute
    public EchoForm initEchoForm() {
        return new EchoForm();
    }

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
                .zipWith(Flux.interval(Duration.ofSeconds(1L)), (msg, c) -> msg);
    }

    @PostMapping("/echo")
    public Mono<Message> echo(@RequestBody Mono<String> body) {
        return body.map(Message::new);
    }

    @PostMapping("/messageflux")
    public Flux<Message> messageFlux(@RequestBody Flux<Message> body) {
        return body.log().zipWith(Flux.interval(Duration.ofSeconds(1L)),
                (msg, c) -> new Message(msg.getMessage() + ":" + c));
    }

    @PostMapping("/echoform")
    public Mono<Message> echoForm(Mono<EchoForm> echoForm) {
        return echoForm.map(e -> new Message(e.getMessage()));
    }
}

