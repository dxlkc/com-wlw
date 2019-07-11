package com.lkc.service.serviceImpl.middlewareImpl;

import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.industry.sensorInfo.Sensor;
import com.lkc.model.industry.sensorInfo.Type;
import com.lkc.service.serviceInterface.MqttService.DowndataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DowndataServiceImpl implements DowndataService {
    @Resource
    DownDataDao downDataDao;

    @Override
    public String addConfirm(String target, String operation, String addr, String code485,
                             String returnLength, String topic, List<Type> types, String deviceId) {
        Sensor sensor = new Sensor();
        sensor.setTypes(types);
        sensor.setReturnLength(returnLength);
        sensor.setOperation(operation);
        sensor.setCode485(code485);
        sensor.setAddr(addr);
        sensor.setTarget(target);
        sensor.setTopic(topic);
        return downDataDao.addConfirm(sensor, deviceId);
    }

    @Override
    public String addTest(String target, String operation, String addr, String code485, String returnLength, String topic, List<Type> types, String deviceId) {
        Sensor sensor = new Sensor();
        sensor.setTypes(types);
        sensor.setReturnLength(returnLength);
        sensor.setOperation(operation);
        sensor.setCode485(code485);
        sensor.setAddr(addr);
        sensor.setTarget(target);
        sensor.setTopic(topic);
        return downDataDao.addTest(sensor, deviceId);
    }
}
