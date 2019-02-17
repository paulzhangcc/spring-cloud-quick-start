package com.paulzhangcc.service;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class UseUserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseUserServerApplication.class, args);
    }

    @Autowired
    DiscoveryClient discoveryClient;


    @Autowired
    ServiceInstanceChooser serviceInstanceChooser;

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

    @GetMapping("/list/instanceslb")
    public ServiceInstance instanceslb(String service) {
        ServiceInstance choose = serviceInstanceChooser.choose(service);
        return choose;
    }

    @Autowired
    UserClient userClient;

    @GetMapping("/list/user")
    public List<User> listUser() {
        return userClient.list();
    }

    @FeignClient("service-user")
    public static interface UserClient {

        @RequestMapping(method = RequestMethod.GET, value = "/user/list")
        public List<User> list();
    }

    public static class User{
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        private Integer age;
    }

}

