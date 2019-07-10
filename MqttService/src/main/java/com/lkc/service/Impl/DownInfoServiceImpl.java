package com.lkc.service.Impl;

import com.lkc.mqttDown.DownHandler;
import com.lkc.service.DownInfoService;
import org.springframework.stereotype.Service;

@Service
public class DownInfoServiceImpl implements DownInfoService {

    public String downData(String message, String deviceId, String uid, String topic) {
        DownHandler downHandler = new DownHandler();
        return downHandler.sendDownMesg(deviceId, uid, topic, message);
    }
}