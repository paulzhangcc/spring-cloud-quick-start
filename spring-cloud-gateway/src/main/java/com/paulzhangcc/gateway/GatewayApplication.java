package com.paulzhangcc.gateway;

import com.paulzhangcc.gateway.filter.MyGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(GatewayApplication.class, args);
        System.out.println(run);
    }

    @Bean
    public RouteLocator route(RouteLocatorBuilder builder, MyGatewayFilter myGatewayFilter) {
        return builder.routes()
                .route(r -> r.path("/service_user/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(myGatewayFilter)
                                .addResponseHeader("X-Response-test", "test"))
                        .uri("lb://SERVICE-USER")
                        .order(0)
                        .id("service_user")
                )
                .build();
    }
}

