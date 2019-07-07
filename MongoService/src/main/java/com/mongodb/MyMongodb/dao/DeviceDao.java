package com.mongodb.MyMongodb.dao;

import com.mongodb.MyMongodb.customAnnontation.NotNull;
import com.mongodb.MyMongodb.model.deviceInfo.Device;
import com.mongodb.MyMongodb.model.deviceInfo.Rule;

import java.util.List;

public interface DeviceDao {

    List<Device> findAllDevice();

    Device findByDeviceId(String deviceId);

    List<Device> findAll(String industryId, String unitId);

    List<Rule> findAllRule(String deviceId);

    /****************更新********************/

    long updateLinkState(String deviceId, String linkState);

    long updateDeviceInfo(String deviceId, String newName, String newRemark);

    long updateLocationDetail(@NotNull String deviceId, String longitude, String latitude, String locationDetail);

    long updateDeviceSendRate(@NotNull String deviceId, String sendRate);

    long updateDeviceOwner(@NotNull String industryId, @NotNull String deviceId, @NotNull String unitId);

    /****************删除********************/

    long deleteAllByIndustryId(String industryId);

    long deleteAllByIndustryIdAndUnitId(String industryId, String unitId);

    long deleteByDeviceId(String industryId, String deviceId);

    /****************添加********************/

    Device addDevice(Device device);

    Device addByDeviceId(String deviceId);


    /****************添加一条规则***********/

    long addRule(String deviceId, Rule rule);

    /****************删除一条规则***********/

    long deleteRule(String deviceId, String ruleId);

    /****************删除全部规则***********/

    long deleteAllRule(String deviceId);
}
