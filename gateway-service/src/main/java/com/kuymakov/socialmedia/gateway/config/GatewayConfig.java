package com.kuymakov.socialmedia.gateway.config;

import com.kuymakov.socialmedia.gateway.filter.AuthGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {
    @Bean
    AuthGatewayFilterFactory getAuthGatewayFilterFactory() {
        return new AuthGatewayFilterFactory();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

}

