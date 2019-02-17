package com.paulzhangcc.discovery;

import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
@RestController
public class Eureka11111ServerApplication {
	public static ConfigurableApplicationContext applicationContext = null;

	@RequestMapping("/test")
	public Object print(){
		return applicationContext.getBean(PeerAwareInstanceRegistry.class).getApplications().getRegisteredApplications();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(Eureka11111ServerApplication.class, args);
		applicationContext = run;
	}
}

