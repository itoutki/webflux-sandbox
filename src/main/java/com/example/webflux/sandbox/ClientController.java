package com.example.webflux.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@RestController
@RequestMapping("/client")
public class ClientController {
    private WebClient webClient;

    public ClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    // Mono(String) API -> bodyToMono OK
    @GetMapping("/gettextmono")
    public Mono<String> getText() {
        return webClient.get()
                .uri("http://localhost:8080/text")
                .retrieve()
                .bodyToMono(String.class);
    }

    // Mono(object) API -> bodyToMono OK
    @GetMapping("/getmessagemono")
    public Mono<Message> getMessageMono() {
        return webClient.get()
                .uri("http://localhost:8080/message")
                .retrieve()
                .bodyToMono(Message.class);
    }

    // Mono(object) API -> bodyToFlux OK
    @GetMapping("/getmessageflux")
    public Flux<Message> getMessageFlux() {
        return webClient.get()
                .uri("http://localhost:8080/message")
                .retrieve()
                .bodyToFlux(Message.class);
    }

    // Flux(array) API  -> bodyToMono NG
    // array型のレスポンスをMessageオブジェクトに変換できないため
//    @GetMapping("/getmessagesmono")
//    public Mono<Message> getMessagesMono() {
//        return webClient.get()
//                .uri("http://localhost:8080/messages")
//                .retrieve()
//                .bodyToMono(Message.class);
//    }

    // Flux(array) API  -> bodyToMono
    // ParameterizedTypeReferenceを使うことで無理やり実現することはできる
    // ただし返ってくるのはMessage型ではなくList<Message>型
    @GetMapping("/getmessagesmono")
    public Mono<ArrayList<Message>> getMessagesMono() {
        return webClient.get()
                .uri("http://localhost:8080/messages")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Message>>() {});
    }

    // Flux(array) API  -> bodyToFlux OK
    @GetMapping("/getmessagesflux")
    public Flux<Message> getMessagesFlux() {
        return webClient.get()
                .uri("http://localhost:8080/messages")
                .retrieve()
                .bodyToFlux(Message.class);
    }

    @GetMapping("/getdelayedtexts")
    public Flux<String> getDelayedText() {
        return webClient.get()
                .uri("http://localhost:8080/delayedtexts")
//                .accept(MediaType.TEXT_EVENT_STREAM)
                .header("ACCEPT", MediaType.TEXT_EVENT_STREAM_VALUE)
                .retrieve()
                .bodyToFlux(String.class);
    }

    @GetMapping("/geteventstream")
    public Flux<String> getEventStream(@RequestParam String api) {
        return webClient.get()
                .uri("http://localhost:8080/" + api)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }

    @GetMapping("/getstreamjson")
    public Flux<Message> getStreamJson(@RequestParam String api) {
        return webClient.get()
                .uri("http://localhost:8080/" + api)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(String.class)
                .map(s -> new Message(s));
    }

    @GetMapping("/post")
    public Mono<Message> post() {
        return webClient.post()
                .uri("http://localhost:8080/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("hello from client"), String.class)
                .retrieve()
                .bodyToMono(Message.class);
    }

    @GetMapping("/postform")
    public Mono<Message> postForm() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("message", "hello from post form");
        return webClient.post()
                .uri("http://localhost:8080/echoform")
//                .body(fromFormData("message", "hello from post form"))
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Message.class);
    }
    
    @GetMapping("/postflux")
    public Flux<Message> postFlux() {
        return webClient.post()
                .uri("http://localhost:8080/messageflux")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .body(Flux.just(new Message("hello"),
                        new Message("world"),
                        new Message("from"),
                        new Message("flux")),
                        Message.class)
                .retrieve()
                .bodyToFlux(Message.class);
    }

    // TODO GET + event-stream
    // TODO GET + stream json
    // TODO POST
    // TODO POST + event-stream
    // TODO POST + stream json
    // TODO Cookie, Header




}
