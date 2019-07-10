package com.mongodb.MyMongodb.controller;

import com.mongodb.MyMongodb.dao.DeviceDao;
import com.mongodb.MyMongodb.model.deviceInfo.Device;
import com.mongodb.MyMongodb.model.deviceInfo.Rule;
import com.mongodb.MyMongodb.service.IndustryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceDao deviceDao;

    @PostMapping(value = "/all")
    public List<Device> findAllDevice() {
        return deviceDao.findAllDevice();
    }

    @PostMapping(value = "/find")
    public Device findByDeviceId(@RequestParam String deviceId) {
        return deviceDao.findByDeviceId(deviceId);
    }

    @PostMapping(value = "/findAll")
    public List<Device> findAll(@RequestParam String industryId, @RequestParam String unitId) {
        return deviceDao.findAll(industryId, unitId);
    }

    @PostMapping(value = "/findRule")
    public List<Rule> findAllRule(@RequestParam String deviceId){
        return deviceDao.findAllRule(deviceId);
    }

    /****************更新********************/

    @PostMapping(value = "/update/linkstate")
    public long updateLinkState(@RequestParam String deviceId, @RequestParam String linkState) {
        return deviceDao.updateLinkState(deviceId, linkState);
    }

    @PostMapping(value = "/update/device")
    public long updateDeviceInfo(@RequestParam String deviceId, @RequestParam String newName,
                                 @RequestParam String newRemark) {
        return deviceDao.updateDeviceInfo(deviceId, newName, newRemark);
    }

    @PostMapping(value = "/update/locationDetail")
    public long updateLocationDetail(@RequestParam String deviceId, @RequestParam String longitude,
                                     @RequestParam String latitude, @RequestParam String locationDetail) {
        return deviceDao.updateLocationDetail(deviceId, longitude, latitude, locationDetail);
    }

    @PostMapping(value = "/update/deviceSendRate")
    public long updateDeviceSendRate(@RequestParam String deviceId, @RequestParam String sendRate) {
        return deviceDao.updateDeviceSendRate(deviceId, sendRate);
    }

    @PostMapping(value = "/update/deviceOwner")
    public long updateDeviceOwner(@RequestParam String industryId, @RequestParam String deviceId,
                                  @RequestParam String unitId) {
        return deviceDao.updateDeviceOwner(industryId, deviceId, unitId);
    }

    /****************删除********************/

    @PostMapping(value = "/delete/device")
    public long deleteByDeviceId(@RequestParam String industryId, @RequestParam String deviceId) {
        return deviceDao.deleteByDeviceId(industryId, deviceId);
    }

    /****************添加********************/

    @PostMapping(value = "/add/userdevice")
    public Device addDevice(@RequestBody Device device) {
        return deviceDao.addDevice(device);
    }

    @PostMapping(value = "/add/device")
    public Device addByDeviceId(@RequestParam String deviceId) {
        return deviceDao.addByDeviceId(deviceId);
    }

    /*******************************************************************************************/
    /************************************             ******************************************/
    /************************************用户自定义规则******************************************/
    /************************************             ******************************************/
    /*******************************************************************************************/

    @PostMapping("/add/rule")
    public long addRule(@RequestParam String deviceId, @RequestBody Rule rule){
        return deviceDao.addRule(deviceId, rule);
    }

    @PostMapping("/update/switchState")
    public long updateSwitchState(@RequestParam String deviceId, @RequestParam String ruleId,
                                  @RequestParam String switchState){
        return deviceDao.updateRuleSwitch(deviceId, ruleId, switchState);
    }

    @PostMapping("/delete/rule")
    public long deleteRule(@RequestParam String deviceId, @RequestParam String ruleId){
        return deviceDao.deleteRule(deviceId, ruleId);
    }

    @PostMapping("/delete/allrule")
    public long deleteAllRule(@RequestParam String deviceId){
        return deviceDao.deleteAllRule(deviceId);
    }

}
