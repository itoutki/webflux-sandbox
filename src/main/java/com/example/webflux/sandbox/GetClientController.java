package com.example.webflux.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.timeout.TimeoutException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.ArrayList;

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

    @GetMapping("/status")
    public Mono<JsonNode> status() {
        Retry retry = Retry.max(3)
                .filter(e -> e instanceof TimeoutException);

        return webClient.get()
                .uri("https://httpbin.org/delay/5")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .retryWhen(retry)
                .onErrorResume(WebClientResponseException.NotFound.class,
                        e -> Mono.just(createMessage(e.getRawStatusCode(), e.getMessage())))
                .onErrorResume(WebClientResponseException.class,
                        e -> Mono.just(createMessage(e.getRawStatusCode(), e.getMessage())))
                .onErrorResume(TimeoutException.class,
                        e -> Mono.just(createMessage(500, "timeout!!!")))
                .onErrorResume(e -> Exceptions.isRetryExhausted(e),
                        e -> Mono.just(createMessage(500, "retry failed!!!" + e.getCause().getClass().toGenericString())))
                .onErrorResume(e -> Mono.just(createMessage(500, "unknown error!!!")));
    }

    private JsonNode createMessage(int status, String message) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("status", status);
        root.put("message", message);
        return root;
    }
}
