package com.example.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.stereotype.Component;
import reactor.netty.http.client.HttpClient;

@Component
public class ReactorNettyHttpClientTimeoutMapper implements ReactorNettyHttpClientMapper {
    @Override
    public HttpClient configure(HttpClient httpClient) {
        return httpClient.tcpConfiguration((tcpClient -> {
            return tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)   // コネクションタイムアウト（ミリ秒で設定）
                .doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(30))     // リードタイムアウト（秒で設定）
                    .addHandlerLast(new WriteTimeoutHandler(30)));  // ライトタイムアウト（秒で設定）
        }));
    }
}
