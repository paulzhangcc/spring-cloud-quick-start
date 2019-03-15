package com.paulzhangcc.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig
public class GatewayApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

