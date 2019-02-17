package com.paulzhangcc.webflux;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
//@EnableDiscoveryClient
@RestController
public class UserWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserWebfluxApplication.class, args);
	}


	@GetMapping("/user/list")
	public Mono<List<User>> list(ServerWebExchange serverWebExchange){
		User user = User.builder().age(18).userName("张三").build();
		List<User> list = new ArrayList<>();
		list.add(user);
		return Mono.just(list);
	}

	@Data
	@Builder
	public static class User{
		private String userName;
		private Integer age;
	}
}

