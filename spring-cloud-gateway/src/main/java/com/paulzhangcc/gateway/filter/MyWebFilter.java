package com.paulzhangcc.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author paul
 * @description
 * @date 2019/1/30
 */
@Component
@Order(0)
public class MyWebFilter implements WebFilter,Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MyWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("===========WebFilter:start");
        return chain.filter(exchange).doOnSuccess(v -> {
            logger.info("===========WebFilter:end");
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
