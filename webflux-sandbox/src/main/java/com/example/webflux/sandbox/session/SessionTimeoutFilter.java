package com.example.webflux.sandbox.session;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class SessionTimeoutFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(SessionTimeoutFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        return webFilterChain.filter(serverWebExchange)
                .transformDeferred(call -> doFilter(serverWebExchange, call));
    }

    private Publisher<Void> doFilter(ServerWebExchange serverWebExchange, Mono<Void> call) {
        return serverWebExchange.getSession().flatMap(webSession -> {
            logger.info("last access time: {}", webSession.getLastAccessTime());
            return call;
        });
//        return Mono.fromRunnable(() -> doBeforeRequest(serverWebExchange))
//                .then()
//                .then(call)
//                .doOnSuccess((done) -> doAfterRequest(serverWebExchange))
//                .doOnError((throwable -> doAfterRequestWithError(serverWebExchange, throwable)));
    }

    private void doBeforeRequest(ServerWebExchange serverWebExchange) {
        logger.info("doBeforeRequest");
        serverWebExchange.getSession()
                .doOnNext(session -> {
                    logger.info("last access time: {}", session.getLastAccessTime());
                    logger.info("session isExpired {}", session.isExpired());
                });
    }

    private void doAfterRequest(ServerWebExchange serverWebExchange) {
        logger.info("doAfterRequest");
    }

    private void doAfterRequestWithError(ServerWebExchange serverWebExchange, Throwable throwable) {
        logger.info("doAfterRequestWithError");

    }
}
