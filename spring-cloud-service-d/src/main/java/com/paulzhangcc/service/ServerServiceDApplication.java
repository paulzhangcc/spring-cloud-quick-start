package com.paulzhangcc.service;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author paul
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
@EnableApolloConfig
public class ServerServiceDApplication {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${name}")
    private String name;

    public static ApplicationContext applicationContext =null;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(ServerServiceDApplication.class, args);

    }

    @GetMapping("/name")
    public String name() {
        return name;
    }


}

