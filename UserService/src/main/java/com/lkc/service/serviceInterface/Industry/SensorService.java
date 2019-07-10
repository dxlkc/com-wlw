package com.lkc.service.serviceInterface.Industry;

import com.lkc.model.industry.sensorInfo.Sensor;
import com.lkc.model.industry.sensorInfo.SensorInfo;

import java.util.List;

public interface SensorService {
    //sensor详情
    String sensordetail(String deviceId, String sensorAddr);

    //更改sensor前返回数据
    String returnbefore(String deviceId, String sensorAddr);

    String findsensor(String deviceId, String addr);

    //查找某divice下的所有sensor
    List<SensorInfo> findAll(String deviceId);

    //添加sensor
    String addsensor(Sensor sensor);

    //添加预设的sensor
    String addDefault(Sensor sensor);

    //删除
    String deletesensor(String deviceId, String sensorAddr);

    //更改最值
    String updateThreshold(String deviceId, String sensorAddr, String type, String max, String min);

    String updateInfo(String deviceId, String sensorAddr, String name);
}
