package com.paulzhangcc.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient(autoRegister = false)
@RestController
public class EurekaServerApplication1 {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(EurekaServerApplication1.class, args);
	}
}

