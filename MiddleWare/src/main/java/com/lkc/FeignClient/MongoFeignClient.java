package com.lkc.FeignClient;

import com.lkc.model.Industry.deviceInfo.Device;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service")
public interface MongoFeignClient {

    @RequestMapping(value = "/industry/find", method = RequestMethod.POST)
    Object findByIndustryId(@RequestParam String id);

    //修改设备位置 经纬度和详细信息
    @RequestMapping(value = "/device/update/locationDetail", method = RequestMethod.POST)
    long updateDeviceLocation(@RequestParam String deviceId, @RequestParam String latitude,
                              @RequestParam String longitude, @RequestParam String locationDetail);

    //修改发送速率
    @RequestMapping(value = "/device/update/deviceSendRate", method = RequestMethod.POST)
    long updateDeviceSendRate(@RequestParam String deviceId, @RequestParam String sendRate);

    //修改传感器最新值
    @RequestMapping(value = "/sensor/update/value", method = RequestMethod.POST)
    long updateSensorValue(@RequestParam String deviceId, @RequestParam String sensorAddr,
                           @RequestParam String type, @RequestParam String value,
                           @RequestParam String time);

    //查找所有设备
    @RequestMapping(value = "/device/all", method = RequestMethod.POST)
    List<Device> findAllDevice();

    //修改设备连接状态
    @RequestMapping(value = "/device/update/linkstate",method = RequestMethod.POST)
    long updateLinkState(@RequestParam String deviceId, @RequestParam String linkState);

    //修改继电器状态
    @PostMapping(value = "/relay/update/state")
    long updatePinsState(@RequestParam String deviceId, @RequestParam String relayAddr,
                         @RequestParam String newPinsState);
}
