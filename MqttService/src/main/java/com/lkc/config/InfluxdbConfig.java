package com.lkc.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:config/influxdb.properties")
public class InfluxdbConfig {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.port}")
    private String port;

    @Value("${influxdb.username}")
    private String username;

    @Value("${influxdb.password}")
    private String password;

    @Value("${influxdb.db}")
    private String dbName;

    @Bean(name = "influxdb")
    public InfluxDB InfluxdbConnect() {
        logger.debug("influxdb : connectting to "+"http://"+url+":"+port);
        InfluxDB influxDB = InfluxDBFactory.connect("http://"+url+":"+port, username, password);
        logger.info("influxdb : connect success");
        return influxDB;
    }

    public String getDbName(){
        return dbName;
    }
}
