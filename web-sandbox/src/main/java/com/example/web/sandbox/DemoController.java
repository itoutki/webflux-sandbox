package com.example.web.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class DemoController {

    private WebClient webClient;

    public DemoController(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://httpbin.org/").build();
    }

    @GetMapping("/wc")
    public Flux<JsonNode> getWebClient(@RequestParam(name = "n", required = false, defaultValue = "1") int n) {
        return Flux.range(0, n)
                .flatMap(i -> webClient.get()
                        .uri("/delay/1")
                        .retrieve()
                        .bodyToFlux(JsonNode.class));
    }

    @GetMapping("/rt")
    public List<JsonNode> getRestTemplate(@RequestParam(name = "n", required = false, defaultValue = "1") int n) {
        RestTemplate restTemplate = new RestTemplate();
        return IntStream.range(0, n)
                .mapToObj(i -> restTemplate.getForObject("https://httpbin.org/delay/1", JsonNode.class))
                .collect(Collectors.toList());
    }
}
