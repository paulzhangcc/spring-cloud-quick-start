package com.paulzhangcc.gateway.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author paul
 * @description
 * @date 2019/3/13
 */
@Component
public class ApolloDynamicRouteBeanPostProcessor implements BeanPostProcessor,
        PriorityOrdered {
    private final static Logger logger = LoggerFactory.getLogger(ApolloDynamicRouteBeanPostProcessor.class);


    private Map<String, GatewayProperties> gatewayPropertiesHashMap = new HashMap<>();

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof GatewayProperties && !AopUtils.isAopProxy(bean)) {
            gatewayPropertiesHashMap.put(beanName, (GatewayProperties) bean);
        }

        if (bean instanceof ApolloDynamicRouteRefresh) {
            ((ApolloDynamicRouteRefresh)bean).setGatewayPropertiesHashMap(gatewayPropertiesHashMap);
        }
        return bean;
    }
}
