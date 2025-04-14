package com.splguyjr.devpathconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class DevpathConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevpathConfigServerApplication.class, args);
	}

}
