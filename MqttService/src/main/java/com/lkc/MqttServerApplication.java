package com.lkc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
public class MqttServerApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MqttServerApplication.class, args);
    }
}
