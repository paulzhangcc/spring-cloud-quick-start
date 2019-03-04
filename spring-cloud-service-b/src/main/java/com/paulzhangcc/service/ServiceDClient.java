package com.paulzhangcc.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author paul
 * @description
 * @date 2019/3/4
 */
@FeignClient("service-d")
public interface ServiceDClient {

    @RequestMapping(method = RequestMethod.GET, value = "/application/name")
    String name();
}