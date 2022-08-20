package com.kuymakov.socialmedia.gateway.filter;

import com.kuymakov.socialmedia.gateway.utils.JwtUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;


@Component
public class AuthGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    public AuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            final List<String> apiEndpoints = List.of("/register", "/login");

            Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                    .noneMatch(uri -> r.getURI().getPath().contains(uri));

            if (isApiSecured.test(request)) {
                if (!request.getHeaders().containsKey("Authorization")) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                final String token = request.getHeaders()
                        .getOrEmpty("Authorization")
                        .get(0)
                        .substring("Bearer ".length());

                if (!jwtUtils.validateToken(token)) {
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                    return response.setComplete();
                }
                String username = jwtUtils.getUserNameFromToken(token);
                var claims = jwtUtils.getClaims(token);
                var roles = (List<String>) claims.get("roles");

                if (!roles.contains(config.getRole())) {
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return response.setComplete();
                }

                exchange.getRequest().mutate()
                        .header("username", username)
                        .headers(h -> h.addAll("roles", roles))
                        .build();

            }
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {

                    }));
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("role");
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Config {
        private String role;
    }
}