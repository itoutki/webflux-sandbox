package com.example.webflux.sandbox.webclient.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
public class WebClientConfig {

    @Bean
    ReactorResourceFactory reactorResourceFactory() {
        return new ReactorResourceFactory();
    }

    @Bean
    ClientHttpConnector clientHttpConnector(ReactorResourceFactory reactorResourceFactory) {
        TcpClient tcpClient = TcpClient.create(reactorResourceFactory.getConnectionProvider())
                .runOn(reactorResourceFactory.getLoopResources())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(2))
                        .addHandlerLast(new WriteTimeoutHandler(2)));
        return new ReactorClientHttpConnector(HttpClient.from(tcpClient).metrics(true));
    }

//    @Bean
//    WebClient webClient(ReactorResourceFactory reactorResourceFactory) {
//        TcpClient tcpClient = TcpClient.create(reactorResourceFactory.getConnectionProvider())
//                .runOn(reactorResourceFactory.getLoopResources())
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
//                .doOnConnected(conn -> conn
//                .addHandlerLast(new ReadTimeoutHandler(2))
//                .addHandlerLast(new WriteTimeoutHandler(2)));
//        ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.from(tcpClient).metrics(true));
//
//        HttpClient httpClient = HttpClient.create()
//                .tcpConfiguration(client -> client
//                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
//                        .doOnConnected(conn -> conn
//                                .addHandlerLast(new ReadTimeoutHandler(2))
//                                .addHandlerLast(new WriteTimeoutHandler(2))));
//        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
//        return WebClient.builder().clientConnector(connector).build();
//    }
}
