package com.freepath.devpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DevpathFeatureServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevpathFeatureServerApplication.class, args);
    }

}
