package com.lkc.mqttDown;

import com.lkc.model.CustomMap.SingleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DownHandler {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public String sendDownMesg(String deviceId, String uid, String topic, String message) {

        lock.lock();

        DownMqtt mqtt = new DownMqtt();
        //设置用户唯一 id
        mqtt.setUid(uid);
        //设置要用的条件变量和对应的锁
        mqtt.setLockAndCondition(lock, condition);
        //设置板子的发布主题
        mqtt.setSub_topic(topic);
        //连接并订阅设置的主题
        mqtt.connect();

        //发布
        //message做base64加密
        Base64.Encoder encoder = Base64.getEncoder();
        String msg = encoder.encodeToString(message.getBytes());

        mqtt.publish(deviceId + "_service", msg);

        //等待 板子返回的信息写入 map
        try {
            condition.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            mqtt.disconnet();
            logger.warn("condition await 发生异常");
            return "接收数据发生异常";
        }
        mqtt.disconnet();

        lock.unlock();
        //从map中取出数据
        SingleMap singleMap = SingleMap.getInstance();
        String res = singleMap.get(uid);
        if (null == res) {
            logger.warn("接收数据超时");
            return "接收数据超时";
        }

        singleMap.delete(uid);
        return res;
    }
}
