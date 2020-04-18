package com.example.webflux.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/client")
public class ClientController {
    private WebClient webClient;

    public ClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/get")
    public Mono<JsonNode> client() {
        return webClient.get()
                .uri("https://httpbin.org/delay/1")
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    @GetMapping("/gettext")
    public Mono<String> getText() {
        return webClient.get()
                .uri("http://localhost:8080/text")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/getdelayedtexts")
    public Flux<String> getDelayedText() {
        return webClient.get()
                .uri("http://localhost:8080/delayedtexts")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }

    // TODO GET + event-stream
    // TODO GET + stream json
    // TODO POST
    // TODO POST + event-stream
    // TODO POST + stream json
    // TODO Cookie, Header




}
