package com.paulzhangcc.gateway;

import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/1/30
 */
public class TestDefaultWebFilterChain {
    public static void main(String[] args) {

        Mono.just(0)
                .map(v->{
                    v= v+1;
                    return v;
                }).log()
                .subscribe(System.out::println);

        if (1 == 1) {
            return;
        }
        //Mono.just("hello").log().then(Mono.just("world").log()).log().subscribe(System.out::println);


        Mono<String> stringMono0 = Mono.just("hello world");
        Mono<String> stringMono1 = Mono.defer(() -> {
            System.out.println(3);
            return stringMono0;
        });

        Mono.fromDirect(
                stringMono1
        ).subscribe(System.out::println);

        if (1 == 1) {
            return;
        }

        if (1 == 1) {
            return;
        }
        WebHandler webHandler = (exchange) -> {
            System.out.println(1);
            return Mono.empty();
        };

        WebFilter webFilter1 = (exchange, chain) -> {
            System.out.println("Filter-1-start");
            return chain.filter(exchange).doOnSuccess(t -> {
                System.out.println("Filter-1-end");
            });
        };

        WebFilter webFilter2 = (exchange, chain) -> {
            System.out.println("Filter-2-start");
            return chain.filter(exchange).doOnSuccess(t -> {
                System.out.println("Filter-2-end");
            });
        };
        List<WebFilter> webFilters = Arrays.asList(webFilter1, webFilter2);


        DefaultWebFilterChain defaultWebFilterChain = new DefaultWebFilterChain(webHandler, webFilters);
        defaultWebFilterChain.filter(null).subscribe();
    }
}
