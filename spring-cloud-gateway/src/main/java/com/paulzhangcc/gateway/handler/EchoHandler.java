package com.paulzhangcc.gateway.handler;

import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author paul
 * @description
 * @date 2019/1/25
 */
@RestController
public class EchoHandler {
    private static final Logger logger = LoggerFactory.getLogger(EchoHandler.class);
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    SpringClientFactory springClientFactory;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RedisTemplate<String,String> stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @GetMapping(value = "/gateway/redis", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Map> redis(ServerWebExchange serverWebExchange) {


        ReactiveValueOperations<String, String> stringStringReactiveValueOperations = reactiveRedisTemplate.opsForValue();

        Hooks.resetOnOperatorDebug();
//        Hooks.onEachOperator((v)->{
//            logger.info("====="+v.toString());
//            return v;
//        });
        Mono<String> name = stringStringReactiveValueOperations.get("name");
        name.subscribe((value)->{
            System.out.println("============"+value);
        });
        //Hooks.resetOnEachOperator();


        Map<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        return Mono.just(map);
    }

    @GetMapping(value = "/gateway/ping", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Map> ping(ServerWebExchange serverWebExchange) {
        Map<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        return Mono.just(map);
    }

    @GetMapping(value = "/gateway/server", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ServiceInstance> server(ServerWebExchange serverWebExchange, @RequestParam String name) {
        ServiceInstance choose = loadBalancerClient.choose(name);
        return Mono.just(choose);
    }

    @GetMapping(value = "/gateway/balancer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<List> balancer(ServerWebExchange serverWebExchange, @RequestParam String name) {
        List<Server> reachableServers = springClientFactory.getLoadBalancer(name).getReachableServers();
        return Mono.just(reachableServers);
    }

    @GetMapping(value = "/gateway/webclient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<String> webclient() {
        return WebClient.create()
                .method(HttpMethod.GET)
                .uri("http://127.0.0.1:9000/user/list")
                .retrieve()
                .bodyToMono(String.class)
                ;
    }

    @GetMapping(value = "/gateway/discovery", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<List> discoveryClient(@RequestParam String name) {
        return Mono.just(discoveryClient.getInstances(name));
    }

    @GetMapping(value = "/gateway/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<String> test() {
        String forObject = lbRestTemplate.getForObject("http://SERVICE-USER/user/list", String.class);
        return Mono.just(forObject);
    }

    @GetMapping(value = "/gateway/test1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<String> test1() {
        String forObject = restTemplate.getForObject("http://127.0.0.1:9000/user/list", String.class);
        return Mono.just(forObject);
    }

    @Autowired(required = false)
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @Autowired(required = false)
    @Qualifier("lbRestTemplate")
    RestTemplate lbRestTemplate;




    @Bean
    @LoadBalanced
    RestTemplate lbRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
