package com.freepath.devpathapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DevpathApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevpathApiGatewayApplication.class, args);
	}

}
