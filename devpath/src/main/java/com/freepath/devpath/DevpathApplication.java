package com.freepath.devpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevpathApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevpathApplication.class, args);
    }

}
