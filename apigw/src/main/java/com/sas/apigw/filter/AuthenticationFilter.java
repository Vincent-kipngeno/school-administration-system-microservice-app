package com.sas.apigw.filter;

import com.sas.apigw.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("route: " + exchange.getRequest().getURI().getPath());
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                try {
                    if (!jwtUtil.isTokenValid(token)) {
                        System.out.println("Expired token...!");
                        throw new RuntimeException("Session expired please login again.");
                    }

                    exchange.getRequest()
                            .mutate()
                            .header("loggedInUser", jwtUtil.extractUsername(token))
                            .build();

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException(e.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
