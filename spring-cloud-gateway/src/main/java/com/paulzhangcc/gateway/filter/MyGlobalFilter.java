package com.paulzhangcc.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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
public class MyGlobalFilter implements GlobalFilter,Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MyGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put("nihao","1");
        logger.info("===========GlobalFilter:start");
        return chain.filter(exchange).doOnSuccess(v -> {
            logger.info("===========GlobalFilter:end");
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
