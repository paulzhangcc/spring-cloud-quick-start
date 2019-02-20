package com.paulzhangcc.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaServer
@RestController
public class EurekaServerApplication2 {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(EurekaServerApplication2.class, args);
	}
}

