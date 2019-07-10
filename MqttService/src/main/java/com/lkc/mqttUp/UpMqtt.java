package com.lkc.mqttUp;

import com.lkc.config.MqttConfig;
import com.lkc.tool.UID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class UpMqtt {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HOST = Config.getHost();
    private static String clientid;
    private MqttClient client;

    //单例
    private static UpMqtt upMqtt = new UpMqtt();

    private UpMqtt() {
    }

    public static UpMqtt getInstance() {
        clientid = UID.getUid();
        return upMqtt;
    }

    public String getHost() {
        return HOST;
    }

    //选项
    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();

        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(false);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(50);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*200秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(12);
        return options;
    }

    //连接并订阅
    protected void connect() throws MqttException {
        MqttConnectOptions options = getOptions();

        //设置主机名，客户端id，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        //设置回调函数
        client.setCallback(new UpCallback());

        if (!client.isConnected()) {
            client.connect(options);
            logger.debug("上行--连接 mqtt proxy 成功");
        }
        //发布时 需要删除
        client.subscribe("6af6188e14aa");
    }

    //发布信息 ok
    public void publish(String topic, String message) {
        try {
            client.publish(topic, new MqttMessage(message.getBytes()));
        } catch (Exception e) {
            logger.warn("上行--发布信息异常 ：" + e.getMessage());
            ;
        }
    }

    //订阅消息
    public void subscribe(String topic) {
        try {
            client.subscribe(topic, 1);
        } catch (MqttException e) {
            logger.warn("上行--订阅发生异常 ：" + e.getMessage());
        }
    }

    //取消订阅
    public void cancelsub(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            logger.warn("上行--取消订阅发生异常 ：" + e.getMessage());
            cancelsub(topic);
        }
    }

    //服务器启动后  第一次连接时使用
    public void firstconnect() {
        while (true) {
            try {
                Thread.sleep(2000);
                connect();
            } catch (MqttException e) {
                logger.warn("上行--无法连接MQTT代理服务器 ：" + e.getMessage());
                continue;
            } catch (Exception e) {
                logger.warn("上行--连接时发生异常 ：" + e.getMessage());
                continue;
            }
            return;
        }
    }

    @Component
    private static class Config {
        @Autowired
        private MqttConfig mqttConfig;

        private static Config config;

        @PostConstruct
        public void init() {
            config = this;
            config.mqttConfig = this.mqttConfig;
        }

        public static String getHost() {
            return config.mqttConfig.getHost();
        }

    }
}
