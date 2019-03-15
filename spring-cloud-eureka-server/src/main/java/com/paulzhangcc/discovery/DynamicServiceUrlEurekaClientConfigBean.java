package com.paulzhangcc.discovery;

import com.ctrip.framework.apollo.ConfigService;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/3/12
 */
@Component
@Primary
public class DynamicServiceUrlEurekaClientConfigBean extends EurekaClientConfigBean {
    private static final String LIST_SEPARATOR = ",";
    public static final String EUREKA_SERVICE_URL = "eureka.service.url";
    protected Splitter splitter = Splitter.on(LIST_SEPARATOR).omitEmptyStrings().trimResults();

    /**
     * @param myZone
     * @return
     */
    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        List<String> urls = eurekaServiceUrls();
        return CollectionUtils.isEmpty(urls) ? super.getEurekaServerServiceUrls(myZone) : urls;
    }

    public List<String> eurekaServiceUrls() {
        String configuration = ConfigService.getAppConfig().getProperty(EUREKA_SERVICE_URL, null);
        if (Strings.isNullOrEmpty(configuration)) {
            return Collections.emptyList();
        }

        return splitter.splitToList(configuration);
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
