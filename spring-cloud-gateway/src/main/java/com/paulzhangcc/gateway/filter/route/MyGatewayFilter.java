package com.paulzhangcc.gateway.filter.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author paul
 * @description
 * @date 2019/1/30
 */
@Component
public class MyGatewayFilter implements GatewayFilter,Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MyGatewayFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("===========GatewayFilter:start");
        return chain.filter(exchange).doOnSuccess(v -> {
            logger.info("===========GatewayFilter:end");
        });
    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
