package com.lkc.FeignClient.mongoservice;

import com.lkc.model.industry.sensorInfo.SensorInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service")
public interface SensorDao {

    @RequestMapping(value = "/sensor/find", method = RequestMethod.POST)
    List<SensorInfo> find(@RequestParam String deviceId, @RequestParam String sensorAddr);

    @RequestMapping(value = "/sensor/find/All", method = RequestMethod.POST)
    List<SensorInfo> findAll(@RequestParam String deviceId);

    //确认账号是否存在addr
    @RequestMapping(value = "/sensor/confirm/addr", method = RequestMethod.POST)
    SensorInfo confirmAddr(@RequestParam String deviceId, @RequestParam String sensorAddr,
                           @RequestParam String type);

    /****************添加********************/
    @RequestMapping(value = "/sensor/add/sensor", method = RequestMethod.POST)
    SensorInfo add(@RequestBody SensorInfo sensorInfo);

    /****************更新********************/
    @RequestMapping(value = "/sensor/update/threshold", method = RequestMethod.POST)
    long updateThreshold(@RequestParam String deviceId, @RequestParam String sensorAddr,
                         @RequestParam String type,
                         @RequestParam String max, @RequestParam String min);

    @RequestMapping(value = "/sensor/update/name", method = RequestMethod.POST)
    long updateName(@RequestParam String deviceId, @RequestParam String sensorAddr,
                    @RequestParam String name);

    /****************删除********************/
    @RequestMapping(value = "/sensor/delete/sensor", method = RequestMethod.POST)
    long delete(@RequestParam String deviceId, @RequestParam String sensorAddr);

}
