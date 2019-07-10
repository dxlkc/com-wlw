package com.lkc.service.serviceInterface.MqttService;

import com.lkc.model.industry.sensorInfo.Type;

import java.util.List;

public interface DowndataService {

    String addTest(String target, String operation, String addr, String code485,
                   String returnLength, String topic, List<Type> types, String deviceId);

    String addConfirm(String target, String operation, String addr, String code485,
                      String returnLength, String topic, List<Type> types, String deviceId);
}
