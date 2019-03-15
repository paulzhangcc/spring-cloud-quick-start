package com.paulzhangcc.gateway;

import reactor.core.publisher.Mono;

/**
 * @author paul
 * @description
 * @date 2019/3/15
 */

public class Test {
    @org.junit.Test
    public void test1(){
        Mono<String> hello = Mono.just("hello");
        hello.subscribe(System.out::println);
    }
}
