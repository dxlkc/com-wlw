package com.lkc.mqttDown;

import com.lkc.mqttUp.UpMqtt;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

//短暂开启  用户进行下发操作
public class DownMqtt {

    private String HOST = UpMqtt.getInstance().getHost();
    private String clientid = String.valueOf(new Random().nextInt(10000));
    //板子返回信息时 发布的主题
    private String sub_topic;
    private MqttClient client;
    //需要传入的必要数据
    private Lock lock;
    private Condition condition;
    private String UID;

    public void setSub_topic(String topic) {
        sub_topic = topic;
    }

    public void setLockAndCondition(Lock alock, Condition cond) {
        lock = alock;
        condition = cond;
    }

    public void setUid(String uid) {
        UID = uid;
    }

    //可选配置
    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setConnectionTimeout(50);
        options.setKeepAliveInterval(12);
        return options;
    }

    //连接
    public void connect() {
        MqttConnectOptions options = getOptions();
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            client.setCallback(new DownCallback(lock, condition, UID));
            client.connect(options);
            client.subscribe(sub_topic);
            //System.out.println("mqtt proxy : 连接成功");
        } catch (MqttException e) {
            //System.out.println("测试连通性 ： 连接 mqtt proxy 失败...");
            e.printStackTrace();
            connect();
        }
    }

    //发布信息
    public void publish(String topic, String message) {
        try {
            client.publish(topic, new MqttMessage(message.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //断开连接
    public void disconnet() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("断开连接失败");
        }
        System.out.println("断开连接成功");
    }
}
