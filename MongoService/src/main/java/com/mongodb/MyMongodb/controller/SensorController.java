package com.mongodb.MyMongodb.controller;

import com.mongodb.MyMongodb.dao.SensorDao;
import com.mongodb.MyMongodb.model.sensorInfo.SensorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/sensor")
public class SensorController {
    @Resource
    private SensorDao sensorDao;

    @PostMapping(value = "/find")
    public List<SensorInfo> find(@RequestParam String deviceId, @RequestParam String sensorAddr) {
        return sensorDao.find(deviceId, sensorAddr);
    }

    @PostMapping(value = "/find/All")
    public List<SensorInfo> findAll(@RequestParam String deviceId) {
        return sensorDao.findAll(deviceId);
    }

    @PostMapping(value = "/find/AllByIndustryId")
    public List<SensorInfo> findAllByIndustryId(@RequestParam String industryId){
        return sensorDao.findAllByIndustryId(industryId);
    }

    @PostMapping(value = "/confirm/addr")
    public SensorInfo confirmAddr(@RequestParam String deviceId, @RequestParam String sensorAddr,
                            @RequestParam String type){
        return sensorDao.findByAddr(deviceId, sensorAddr, type);
    }

    /****************添加********************/

    @PostMapping(value = "/add/sensor")
    public SensorInfo add(@RequestBody SensorInfo sensorInfo) {
        return sensorDao.add(sensorInfo);
    }

    /****************更新********************/

    @PostMapping(value = "/update/name")
    public long updateName(@RequestParam String deviceId, @RequestParam String sensorAddr,
                           @RequestParam String name){
        return sensorDao.updateName(deviceId, sensorAddr, name);
    }

    @PostMapping(value = "/update/threshold")
    public long updateThreshold(@RequestParam String deviceId, @RequestParam String sensorAddr,
                                @RequestParam String type,
                                @RequestParam String max, @RequestParam String min) {
        return sensorDao.updateThreshold(deviceId, sensorAddr, type, max, min);
    }

    @PostMapping(value = "/update/value")
    public long updateValue(@RequestParam String deviceId, @RequestParam String sensorAddr,
                            @RequestParam String type, @RequestParam String value, @RequestParam String time) {
        return sensorDao.updateValue(deviceId, sensorAddr, type, value, time);
    }

    /****************删除********************/

    @PostMapping(value = "/delete/sensor")
    public long delete(@RequestParam String deviceId, @RequestParam String sensorAddr) {
        return sensorDao.delete(deviceId, sensorAddr);
    }
}
