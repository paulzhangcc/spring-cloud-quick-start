package com.paulzhangcc.service;

import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author paul
 * @description
 * @date 2019/2/15
 */
@RestController
@Configuration
public class HealthCheckConroller {

    public InstanceInfo.InstanceStatus  status = InstanceInfo.InstanceStatus.UP;
    @RequestMapping("/health/status")
    public String status(){
        return status.name();
    }

    @RequestMapping("/health/up")
    public String up(){
        status = InstanceInfo.InstanceStatus.UP;
        return status.name();
    }
    @RequestMapping("/health/down")
    public String down(){
        status = InstanceInfo.InstanceStatus.DOWN;
        return status.name();
    }

    @Bean
    public HealthCheckHandler customHealthCheckHandler(){
        return new HealthCheckHandler(){
            @Override
            public InstanceInfo.InstanceStatus getStatus(InstanceInfo.InstanceStatus currentStatus) {
                return status;
            }
        };
    }

}
