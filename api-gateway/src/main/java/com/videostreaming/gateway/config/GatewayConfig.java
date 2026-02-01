package com.videostreaming.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("account-service", r -> r.path("/api/account", "/api/account/**")
                        .uri("lb://account-service") )
                .route("video-service", r -> r.path("/api/video", "/api/video/**")
                        .uri("lb://video-service"))
                .build();
    }
}