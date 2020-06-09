package com.example.webflux.sandbox.session;

import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class CustomInMemoryWebSessionStore extends InMemoryWebSessionStore {

    @Override
    public Mono<WebSession> createWebSession() {
        return super.createWebSession()
                .map(session -> {
                    session.setMaxIdleTime(Duration.ofSeconds(30L));
                    return session;
                });
    }
}
