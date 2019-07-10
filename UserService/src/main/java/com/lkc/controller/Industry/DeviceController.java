package com.lkc.controller.Industry;

import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.service.serviceInterface.Industry.DeviceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @Resource
    private DownDataDao downDataDao;

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public String findByDeviceId(@RequestParam String deviceId) {
        return deviceService.findDevice(deviceId);
    }

    @RequestMapping(value = "/findall", method = RequestMethod.POST)
    public String findAll(@RequestParam String industryId, @RequestParam String unitId) {
        return deviceService.findataALl(industryId, unitId);
    }

    /****************更新********************/

    @RequestMapping(value = "/update/device", method = RequestMethod.POST)
    public String updateDeviceInfo(@RequestParam String industryId, @RequestParam String deviceId,
                                   @RequestParam String newName, @RequestParam String newRemark,
                                   @RequestParam String newSendrate) {
        return deviceService.updateDeviceInfo(industryId, deviceId, newName, newRemark, newSendrate);
    }

    @RequestMapping(value = "/operate/rule", method = RequestMethod.POST)
    public String operateRule(@RequestParam String industryId, @RequestParam String deviceId,
                              @RequestParam String id, @RequestParam Boolean operation) {
        return deviceService.operaterule(industryId, deviceId, id, operation);
    }

    /****************删除********************/

    @RequestMapping(value = "/delete/device", method = RequestMethod.POST)
    public String deleteByDeviceId(@RequestParam String industryId, @RequestParam String deviceId) {
        return deviceService.deleteDevice(industryId, deviceId);
    }

    @RequestMapping(value = "/delete/rule", method = RequestMethod.POST)
    public String deleteRule(@RequestParam String industryId, @RequestParam String deviceId,
                             @RequestParam String id) {
        return deviceService.deleterule(industryId, deviceId, id);
    }

    /****************添加********************/

    @RequestMapping(value = "/add/device", method = RequestMethod.POST)
    public String addDevice(@RequestParam String industryId, @RequestParam String unitId,
                            @RequestParam String deviceId, @RequestParam String deviceName,
                            @RequestParam String deviceRemark, @RequestParam String sendRate) {
        String res;
//        res = deviceService.findDevice(deviceId);
//        if (res == null) {
        res = deviceService.addDevice(industryId, unitId, deviceId, deviceName, deviceRemark, sendRate);
//        } else {
//            deviceService.updateDeviceInfo(industryId,deviceId, deviceName, deviceRemark,sendRate);
//        }
        downDataDao.subscribe(deviceId);
        return res;
    }

    @RequestMapping(value = "/add/rule", method = RequestMethod.POST)
    public String addRule(@RequestBody Map<String, String> map) {
        return deviceService.addrule(map);
    }

    /*********************测试连接************************/

    @RequestMapping(value = "/linkstate", method = RequestMethod.POST)
    public String linkstate(@RequestParam String deviceId) {
        return deviceService.heart(deviceId);
    }


    /*********************查询日志************************/

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public String findLog(@RequestParam String industryId, @RequestParam String deviceId,
                          @RequestParam String start, @RequestParam String end) {
        return deviceService.findLog(industryId, deviceId, start, end);
    }

}
