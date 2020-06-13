package com.example.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WebClientSampleTest {

    private WebClient.Builder webClientBuilder;

    @Autowired
    public WebClientSampleTest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Test
    public void testGet() throws Exception {
        WebClientSample webClientSample = new WebClientSample(webClientBuilder);

        JsonNode actual = webClientSample.get();
        System.out.println(actual.toPrettyString());

        assertEquals("https://httpbin.org/delay/1", actual.get("url").textValue());
    }

    @Test
    public void testPost() throws Exception {
        WebClientSample webClientSample = new WebClientSample(webClientBuilder);

        JsonNode actual = webClientSample.post();
        System.out.println(actual.toPrettyString());

        assertAll(
                () -> assertEquals("https://httpbin.org/delay/1", actual.get("url").textValue()),
                () -> assertEquals("{\"message\":\"Hello WebClient\"}", actual.get("data").textValue())
        );
    }
}
