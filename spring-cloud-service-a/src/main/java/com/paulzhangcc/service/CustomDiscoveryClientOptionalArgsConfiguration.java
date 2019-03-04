package com.paulzhangcc.service;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaEventListener;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

/**
 * @author paul
 * @description
 * @date 2019/2/15
 */
//@Configuration
@AutoConfigureBefore(DiscoveryClientOptionalArgsConfiguration.class)
public class CustomDiscoveryClientOptionalArgsConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext = null;

    @Bean
    @ConditionalOnMissingClass("com.sun.jersey.api.client.filter.ClientFilter")
    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs() {
        RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs = new RestTemplateDiscoveryClientOptionalArgs();
        Set<EurekaEventListener> eurekaEventListeners = new HashSet();
//        eurekaEventListeners.add(event -> {
//            if (event instanceof StatusChangeEvent) {
//                System.out.println(event);
//            }
//        });
        restTemplateDiscoveryClientOptionalArgs.setEventListeners(eurekaEventListeners);
        return restTemplateDiscoveryClientOptionalArgs;
    }

    @Bean
    @ConditionalOnClass(name = "com.sun.jersey.api.client.filter.ClientFilter")
    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs() {
        MutableDiscoveryClientOptionalArgs mutableDiscoveryClientOptionalArgs = new MutableDiscoveryClientOptionalArgs();

        Set<EurekaEventListener> eurekaEventListeners = new HashSet();
//        eurekaEventListeners.add(event -> {
//            if (event instanceof StatusChangeEvent) {
//                System.out.println(event);
//            }
//        });
        mutableDiscoveryClientOptionalArgs.setEventListeners(eurekaEventListeners);
        return mutableDiscoveryClientOptionalArgs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
