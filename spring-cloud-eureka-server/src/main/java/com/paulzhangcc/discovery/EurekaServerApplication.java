package com.paulzhangcc.discovery;

import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient(autoRegister = false)
@RestController
public class EurekaServerApplication {
	public static ConfigurableApplicationContext applicationContext = null;

	@RequestMapping("/test")
	public Object print(){
		return applicationContext.getBean(PeerAwareInstanceRegistry.class).getApplications().getRegisteredApplications();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(EurekaServerApplication.class, args);
		applicationContext = run;
	}
}

