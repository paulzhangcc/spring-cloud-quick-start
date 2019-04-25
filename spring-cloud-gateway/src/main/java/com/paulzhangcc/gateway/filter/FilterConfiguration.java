package com.paulzhangcc.gateway.filter;

import com.paulzhangcc.gateway.filter.web.IpBlacklistFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author paul
 * @description
 * @date 2019/3/19
 */
@Configuration
public class FilterConfiguration {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 500)
    public IpBlacklistFilter ipBlacklistFilter() {
        return new IpBlacklistFilter();
    }

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE + 1000)
//    public CorsWebFilter corsWebFilter() {
//        //设置允许跨域
//        return new CorsWebFilter((exchange) -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.addAllowedMethod("*");
//            configuration.addAllowedOrigin("*");
//            configuration.addAllowedHeader("*");
//            configuration.setAllowCredentials(true);
//            return configuration;
//        });
//    }

    //    @Bean
//    public PasswordEncoder passwordEncoder(){
//
//    }
    public Mono<ServerResponse> getUser(ServerRequest request) {
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just("hello world"), String.class);
    }

    @Bean
    public RouterFunction<ServerResponse> routeCity() {
        return RouterFunctions
                .route(RequestPredicates.GET("/test")
                                .and(RequestPredicates.accept(APPLICATION_JSON)),
                        this::getUser);
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange()
                .pathMatchers("/monitor/**").hasAuthority("ADMIN")
                .pathMatchers("/test").permitAll()
                .pathMatchers("/gateway/**").permitAll()
                .anyExchange().authenticated()

                .and().cors().configurationSource(
                (exchange) -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedMethod("*");
                    configuration.addAllowedOrigin("*");
                    configuration.addAllowedHeader("*");
                    configuration.setAllowCredentials(true);
                    return configuration;
                })
                .and().httpBasic()
                .and().csrf().disable()
                .formLogin();
        return http.build();


    }
}
