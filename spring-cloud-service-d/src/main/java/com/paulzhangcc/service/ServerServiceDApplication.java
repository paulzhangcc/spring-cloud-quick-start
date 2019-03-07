package com.paulzhangcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author paul
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class ServerServiceDApplication {

    @Value("${spring.application.name}")
    private String applicationName;

    public static ApplicationContext applicationContext =null;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(ServerServiceDApplication.class, args);

    }

    @Autowired
    ServiceAClient serviceAClient;

    @GetMapping("/test/name")
    public String list(HttpServletRequest httpServletRequest) {
        return applicationName + ":" + serviceAClient.name();
    }

    @GetMapping("/map")
    public Map map(Map map) {
        return map;
    }

    @GetMapping("/model")
    public Model map(Model map) {
        return map;
    }

}

