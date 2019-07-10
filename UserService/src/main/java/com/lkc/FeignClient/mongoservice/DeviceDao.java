package com.lkc.FeignClient.mongoservice;

import com.lkc.model.industry.deviceInfo.Device;
import com.lkc.model.industry.deviceInfo.Rule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service")
public interface DeviceDao {

    @PostMapping(value = "/device/find")
    Device findByDeviceId(@RequestParam String deviceId);

    @PostMapping(value = "/device/findAll")
    List<Device> findAll(@RequestParam String industryId, @RequestParam String unitId);

    @PostMapping(value = "/device/findRule")
    List<Rule> findAllRule(@RequestParam String deviceId);

    /****************更新********************/
    @PostMapping(value = "/device/update/device")
    long updateDeviceInfo(@RequestParam String deviceId,
                          @RequestParam String newName, @RequestParam String newRemark);

    @PostMapping(value = "/device/update/location")
    long updateDeviceLocation(@RequestParam String deviceId,
                              @RequestParam String longitude, @RequestParam String latitude);

    @PostMapping(value = "/device/update/deviceSendRate")
    long updateDeviceSendRate(@RequestParam String deviceId,
                              @RequestParam String sendRate);

    @PostMapping(value = "/device/update/deviceOwner")
    long updateDeviceOwner(@RequestParam String industryId, @RequestParam String deviceId,
                           @RequestParam String unitId);


    /****************删除********************/
    @PostMapping(value = "/device/delete/device")
    long deleteByDeviceId(@RequestParam String industryId, @RequestParam String deviceId);


    @PostMapping(value = "/device/delete/rule")
    long deleteRule(@RequestParam String deviceId, @RequestParam String ruleId);

    @PostMapping(value = "/device/delete/allrule")
    long deleteAllrule(@RequestParam String deviceId);

    /****************添加********************/
    @PostMapping(value = "/device/add/userdevice")
    Device addDevice(@RequestBody Device device);

    @PostMapping(value = "/device/add/device")
    Device addByDeviceId(@RequestParam String deviceId);

    @PostMapping(value = "/device/add/rule")
    long addRule(@RequestParam String deviceId, @RequestBody Rule rule);

    @PostMapping(value = "/device/update/switchState")
    long updateRuleSwitch(@RequestParam String deviceId,
                          @RequestParam String ruleId, @RequestParam String switchState);

}
