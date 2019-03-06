package com.paulzhangcc.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/3/6
 */
@FeignClient("service-b")
public interface ServiceBClient {

    @RequestMapping(method = RequestMethod.GET, value = "/test/name")
    public List<String> name();
}
