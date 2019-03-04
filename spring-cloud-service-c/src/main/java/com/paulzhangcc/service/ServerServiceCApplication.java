package com.paulzhangcc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author paul
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ServerServiceCApplication {

    @Value("${spring.application.name}")
    private String applicationName;

    public static void main(String[] args) {
        SpringApplication.run(ServerServiceCApplication.class, args);
    }

    @GetMapping("/application/name")
    public String list() {
       return applicationName;
    }

}

