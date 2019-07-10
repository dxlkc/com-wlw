package com.lkc.service.serviceInterface.MqttService;

import java.util.Map;

public interface ThresholdService {

    void thresholdHandler(String deviceId, Map<String, String> map, String time);
}
