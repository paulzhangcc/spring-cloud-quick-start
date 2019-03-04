package com.paulzhangcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

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

    @GetMapping("/application/name")
    public List<String> listUser() {
        List<String> list = new ArrayList<>();
        list.add(applicationName);
        list.add(serviceCClient.name());
        list.add(serviceDClient.name());
        return list;
    }


}

