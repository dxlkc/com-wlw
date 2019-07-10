package com.lkc.start;

import com.lkc.coap.Coap;
import com.lkc.heartbeat.HeartBeat;
import com.lkc.mqttUp.UpMqtt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Connect implements CommandLineRunner {
    @Resource
    private HeartBeat heartBeat;

    @Override
    public void run(String... args) throws Exception {
        //连接到mqtt代理服务器
        UpMqtt.getInstance().firstconnect();

        //轮询判断 设备连接状态
        heartBeat.start();
    }
}