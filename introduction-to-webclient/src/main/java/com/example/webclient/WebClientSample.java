package com.example.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientSample {
    public static final Logger logger = LoggerFactory.getLogger(WebClientSample.class);

    private WebClient webClient;

    public WebClientSample(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public JsonNode get() {
        return webClient.get()                              // リクエストメソッドを指定
                .uri("https://httpbin.org/delay/1")      // URLを指定
                .retrieve()                                 // リクエスト実行
                .bodyToMono(JsonNode.class)                 // レスポンスをオブジェクト（リアクティブ型）にマッピング
                .block();                                   // リアクティブ型から通常のオブジェクトを取得
    }

    public JsonNode post() {
        return webClient.post()                             // リクエストメソッドを指定
                .uri("https://httpbin.org/delay/1")      // URLを指定
                .contentType(MediaType.APPLICATION_JSON)    // コンテンツタイプを指定
                .bodyValue(new Message("Hello WebClient"))  // POSTのボディを指定
                .retrieve()                                 // リクエスト実行
                .bodyToMono(JsonNode.class)                 // レスポンスをオブジェクト（リアクティブ型）にマッピング
                .block();                                   // リアクティブ型から通常のオブジェクトを取得
    }

    class Message {
        private String message;

        public Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
