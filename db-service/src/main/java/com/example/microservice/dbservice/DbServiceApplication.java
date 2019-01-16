package com.example.microservice.dbservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableJpaRepositories(basePackages = {"com.example.microservice.dbservice.repository"})

public class DbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbServiceApplication.class, args);
	}

}

