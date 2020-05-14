package com.example.web.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private WebClient webClient;
    private AsyncApiService asyncApiService;

    public DemoController(WebClient.Builder builder, AsyncApiService asyncApiService) {
        webClient = builder.baseUrl("https://httpbin.org/").build();
        this.asyncApiService = asyncApiService;
    }

    @GetMapping("/wc")
    public List<JsonNode> getWebClient(@RequestParam(name = "n", required = false, defaultValue = "1") int n) {
        return Flux.range(0, n)
                .flatMap(i -> webClient.get()
                        .uri("/delay/1")
                        .retrieve()
                        .bodyToFlux(JsonNode.class)).collectList().block();
    }

    @GetMapping("/rt")
    public List<JsonNode> getRestTemplate(@RequestParam(name = "n", required = false, defaultValue = "1") int n) {
        RestTemplate restTemplate = new RestTemplate();
        return IntStream.range(0, n)
                .mapToObj(i -> restTemplate.getForObject("https://httpbin.org/delay/1", JsonNode.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/rtasync")
    public List<JsonNode> getRestTemplateAsync(@RequestParam(name = "n", required = false, defaultValue = "1") int n)
        throws Exception {
        logger.warn("start");

        List<CompletableFuture<JsonNode>> futures = new ArrayList<>();
        for (int i = 0; i < n ; i++) {
            futures.add(asyncApiService.get());
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[n])).join();

        List<JsonNode> results = new ArrayList<>();
        for (CompletableFuture<JsonNode> future : futures) {
            results.add(future.get());
        }
        logger.warn("end");
        return results;
    }
}
