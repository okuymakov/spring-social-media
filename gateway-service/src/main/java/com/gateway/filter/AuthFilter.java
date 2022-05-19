package com.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import com.gateway.utils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthFilter implements GatewayFilter {

    private final JwtUtils jwtUtils;

    public AuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

        final List<String> apiEndpoints = List.of("/register", "/login");

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            try {
                jwtUtils.validateToken(token);
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
            String username = jwtUtils.getUserNameFromToken(token);
            exchange.getRequest().mutate().header("username", username).build();
        }
        return chain.filter(exchange);
    }

}