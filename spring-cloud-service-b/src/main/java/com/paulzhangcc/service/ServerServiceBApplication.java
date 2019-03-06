package com.paulzhangcc.service;

import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class ServerServiceBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerServiceBApplication.class, args);
    }

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    ServiceCClient serviceCClient;

    @Autowired
    ServiceDClient serviceDClient;

    @GetMapping("/test/name")
    public List<String> listUser() {
        List<String> list = new ArrayList<>();
        list.add(applicationName);
        list.add(serviceCClient.name());
        list.add(serviceDClient.name());
        return list;
    }

}

