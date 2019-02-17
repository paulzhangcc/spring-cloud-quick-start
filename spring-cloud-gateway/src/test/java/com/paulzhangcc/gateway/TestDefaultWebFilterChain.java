package com.paulzhangcc.gateway;

import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Flux;
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
        Flux.just(1,2,3,4).concatMap(t->{
            int i = t.intValue();
            return  Mono.just("hello:"+i);
        }).next().subscribe(System.out::println);
        System.out.println("==================================");

        WebHandler webHandler = (exchange)->{
            System.out.println(1);
            return Mono.empty();
        };

        WebFilter webFilter1 = (exchange,chain)->{
            System.out.println("Filter-1-start");
            return chain.filter(exchange).doOnSuccess(t->{
                System.out.println("Filter-1-end");
            });
        };

        WebFilter webFilter2 = (exchange,chain)->{
            System.out.println("Filter-2-start");
            return chain.filter(exchange).doOnSuccess(t->{
                System.out.println("Filter-2-end");
            });
        };
        List<WebFilter> webFilters = Arrays.asList(webFilter1, webFilter2);


        DefaultWebFilterChain defaultWebFilterChain = new DefaultWebFilterChain(webHandler, webFilters);
        defaultWebFilterChain.filter(null).subscribe();
    }
}
