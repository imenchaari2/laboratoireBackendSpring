package com.example.gatewayservice.security.config;

import com.example.gatewayservice.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Autowired
	private JwtAuthenticationFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route( r -> r.path("/api/member/**").filters(f -> f.filter(filter)).uri("lb://MEMBER-SERVICE"))
				.route( r -> r.path("/api/auth/**").filters(f -> f.filter(filter)).uri("lb://MEMBER-SERVICE"))
				.route( r -> r.path("/api/article/**").filters(f -> f.filter(filter)).uri("lb://ARTICLE-SERVICE"))
				.route( r -> r.path("/api/tool/**").filters(f -> f.filter(filter)).uri("lb://TOOL-SERVICE"))
				.route( r -> r.path("/api/event/**").filters(f -> f.filter(filter)).uri("lb://EVENT-SERVICE"))
				.build();
	}

}
