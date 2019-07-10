package com.lkc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:config/coap.properties")
public class CoapConfig {

    @Value("${coap.server.ip}")
    private String ip;

    public String getServerIp(){
        return ip;
    }
}
