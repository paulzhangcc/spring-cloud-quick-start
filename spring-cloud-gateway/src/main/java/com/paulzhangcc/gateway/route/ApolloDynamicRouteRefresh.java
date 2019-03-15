package com.paulzhangcc.gateway.route;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author paul
 * @description
 * @date 2019/3/13
 */
@Component
public class ApolloDynamicRouteRefresh implements ApplicationContextAware {

    @Autowired
    ConfigurationPropertiesBindingPostProcessor configurationPropertiesBindingPostProcessor;

    private final static Logger logger = LoggerFactory.getLogger(ApolloDynamicRouteBeanPostProcessor.class);

    @ApolloConfig
    private Config config;

    public void setGatewayPropertiesHashMap(Map<String, GatewayProperties> gatewayPropertiesHashMap) {
        this.gatewayPropertiesHashMap = gatewayPropertiesHashMap;
    }

    private Map<String, GatewayProperties> gatewayPropertiesHashMap = new HashMap<>();

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshGatewayProperties();
    }

    private void refreshGatewayProperties() {
        Set<String> keyNames = this.config.getPropertyNames();
        boolean flag = false;
        for (String key : keyNames) {
            if (containsIgnoreCase(key, "spring.cloud.gateway")) {
                flag = true;
                break;
            }
        }
        if (!flag || applicationContext == null || configurationPropertiesBindingPostProcessor == null) {
            return;
        }
        gatewayPropertiesHashMap.forEach((key, value) -> {
            logger.info("路由配置修改之前的数据:" + value);
            configurationPropertiesBindingPostProcessor.postProcessBeforeInitialization(value, key);
            logger.info("路由配置修改之后的数据:" + value);
        });
        applicationContext.publishEvent(new RefreshRoutesEvent(this));

    }

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
