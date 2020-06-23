package com.example.webflux.sandbox.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/security/public").permitAll()
                .pathMatchers("/security/private").authenticated()
                .anyExchange().permitAll()
                .and()
                .formLogin()
                .and()
                .build();
    }
}
