package com.paulzhangcc.config;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.spi.MetaServerProvider;
import com.ctrip.framework.apollo.core.spi.Ordered;

/**
 * @author paul
 * @description
 * @date 2019/3/12
 */
public class CustomMetaServerProvider implements MetaServerProvider {
    @Override
    public String getMetaServerAddress(Env env) {
        if (env == null){
            return null;
        }
        if (Env.DEV == env){
            return "http://127.0.0.1:8080";
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
