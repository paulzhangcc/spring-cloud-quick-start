package com.paulzhangcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
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

/**
 * @author paul
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class ServerServiceAApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerServiceAApplication.class, args);
    }

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("/list/instanceslb")
    public ServiceInstance instanceslb(String service) {
        return loadBalancerClient.choose(service);
    }

    @GetMapping("/list/exe")
    public String exe(String service) throws IOException {
        return loadBalancerClient.execute(service,
                //只是为什么测试负载均衡,所以模仿去请求服务提供者
                serviceInstance -> serviceInstance.getUri().toString());
    }

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/list/services")
    public List<String> services() {
        List<String> services = discoveryClient.getServices();
        return services;
    }

    @GetMapping("/list/instances")
    public List<ServiceInstance> instances(String service) {
        List<ServiceInstance> instances = discoveryClient.getInstances(service);
        return instances;
    }

    @Value("${spring.application.name}")
    private String applicationName;


    @Autowired
    ServiceBClient serviceBClient;

    @GetMapping("/application/name")
    public List<String> applications() {
        List<String> list = new ArrayList<>();
        list.add(applicationName);
        list.addAll(serviceBClient.name());
        return list;
    }

    @FeignClient("service-b")
    public static interface ServiceBClient {

        @RequestMapping(method = RequestMethod.GET, value = "/application/name")
        public List<String> name();
    }


}

