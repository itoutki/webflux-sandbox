package com.example.webflux.sandbox;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
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
@RequestMapping("/getclient")
public class GetClientController {
    private WebClient webClient;

    public GetClientController(WebClient webClient) {
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

    @GetMapping("/cookieheader")
    public Mono<JsonNode> getCookieHeader() {
        return webClient.get()
                .uri("https://httpbin.org/get")
                .cookie("mycookie", "hello-cookie")
                .cookies(cookies -> {
                    cookies.add("mysecondcookie", "hello-cookie2");
                    cookies.add("mythirdcookie", "hello-cookie3");
                })
                .header("myheader", "hello-header")
                .headers(headers -> {
                    headers.add("mysecondheader", "hello-header2");
                    headers.add("mythirdheader", "hello-header3");
                })
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
