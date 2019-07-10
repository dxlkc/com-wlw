package com.lkc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:config/mqtt.properties")
public class MqttConfig {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${mqtt.ip}")
    private String ip;

    @Value("${mqtt.port}")
    private Integer port;

    public String getHost() {
        logger.info("mqtt proxy : tcp://" + ip + ":" + port);
        return "tcp://" + ip + ":" + port;
    }

    public String Host(){
        return "tcp://" + ip + ":" + port;
    }
}
