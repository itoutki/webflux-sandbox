package com.example.web.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncApiService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncApiService.class);

    private RestTemplate restTemplate;

    public AsyncApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Async
    public CompletableFuture<JsonNode> get() {
        logger.warn("start");
        JsonNode root = restTemplate.getForObject("https://httpbin.org/delay/1", JsonNode.class);
        logger.warn("end");
        return CompletableFuture.completedFuture(root);
    }
}
