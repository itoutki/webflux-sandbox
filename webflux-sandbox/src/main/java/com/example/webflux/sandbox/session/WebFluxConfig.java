package com.example.webflux.sandbox.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionManager;

@Configuration
public class WebFluxConfig {

    @Bean
    public WebSessionManager webSessionManager() {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionStore(new CustomInMemoryWebSessionStore());
        return webSessionManager;
    }
}
