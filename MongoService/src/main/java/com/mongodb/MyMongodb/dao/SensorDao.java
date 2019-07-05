package com.mongodb.MyMongodb.dao;

import com.mongodb.MyMongodb.model.sensorInfo.SensorInfo;

import java.util.List;

public interface SensorDao {

    List<SensorInfo> find(String deviceId, String sensorAddr);

    List<SensorInfo> findAll(String deviceId);

    List<SensorInfo> findAllByIndustryId(String industryId);

    SensorInfo findByAddr(String deviceId, String sensorAddr, String type);

    /****************添加********************/

    SensorInfo add(SensorInfo sensorInfo);

    /****************更新********************/

    long updateName(String deviceId, String sensorAddr, String name);

    long updateThreshold(String deviceId, String sensorAddr, String type, String max, String min);

    long updateValue(String deviceId, String sensorAddr, String type, String value, String time);

    /****************删除********************/

    long deleteAllByIndustryId(String industryId);

    long deleteAllByIndustryIdAndUnitId(String industryId, String unitId);

    long deleteAllByDeviceId(String deviceId);

    long delete(String deviceId, String sensorAddr);

}
