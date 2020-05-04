package com.example.webflux.sandbox.webclient.client;

import com.example.webflux.sandbox.webclient.server.Message;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/postclient")
public class PostClientController {
    private WebClient webClient;

    public PostClientController(WebClient webClient) {
        this.webClient = webClient;
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

    @GetMapping("/postmultipart")
    public Mono<Message> postMultipart() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("meta-data", "sample");
        builder.part("file-data", new ClassPathResource("application.yml"));
        MultiValueMap<String, HttpEntity<?>> parts = builder.build();

        return webClient.post()
                .uri("http://localhost:8080/multipart")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(parts)
                .retrieve()
                .bodyToMono(String.class)
                .map(Message::new);
    }
}
