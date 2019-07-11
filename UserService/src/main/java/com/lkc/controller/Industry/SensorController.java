package com.lkc.controller.Industry;

import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.industry.sensorInfo.Sensor;
import com.lkc.service.serviceInterface.Industry.SensorService;
import com.lkc.FeignClient.mongoservice.SensorDao;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sensor")
public class SensorController {
    @Resource
    private SensorService sensorService;
    @Resource
    private SensorDao sensorDao;
    @Resource
    private DownDataDao downDataDao;

    //sensor详情页
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public String sensordetail(@RequestParam String deviceId, @RequestParam String sensorAddr) {
        return sensorService.sensordetail(deviceId, sensorAddr);
    }

    //sensor 更改阈值前返回数据
    @RequestMapping(value = "/returnbefore", method = RequestMethod.POST)
    public String returnbefore(@RequestParam String deviceId, @RequestParam String sensorAddr) {
        return sensorService.returnbefore(deviceId, sensorAddr);
    }

    @RequestMapping(value = "/find/sensor", method = RequestMethod.POST)
    public String findsensor(@RequestParam String deviceId, @RequestParam String sensorAddr) {
        return sensorService.findsensor(deviceId, sensorAddr);
    }

    /****************添加********************/

    @RequestMapping(value = "/add/sensor", method = RequestMethod.POST)
    public String add(@RequestBody Sensor sensor) {
        System.out.println("addconfirm" + sensor.getReturnLength() + "types");
        return sensorService.addsensor(sensor);

    }

    @RequestMapping(value = "/add/default", method = RequestMethod.POST)
    public String addefault(@RequestBody Sensor sensor) {
        return sensorService.addDefault(sensor);
    }

    /****************更新********************/

    @RequestMapping(value = "/update/sensor", method = RequestMethod.POST)
    public String updateThreshold(@RequestBody List<Map<String, String>> mapList) {

        if (mapList == null || mapList.size() == 0) {
            return "fail";
        }

        String res = sensorService.updateInfo(mapList.get(0).get("deviceId"), mapList.get(0).get("sensorAddr"),
                mapList.get(0).get("name"));

        if (res.equals("fail")) {
            return res;
        }

        for (Map<String, String> map : mapList) {
            if (!(map.get("max") == null || map.get("min") == null)) {
                res = sensorService.updateThreshold(map.get("deviceId"), map.get("sensorAddr"), map.get("typeName"),
                        map.get("max"), map.get("min"));
                if (!res.equals("success")) {
                    return res;
                }
            }
        }
        return "success";
    }

    /****************删除********************/

    @RequestMapping(value = "/delete/sensor", method = RequestMethod.POST)
    public String delete(@RequestParam(name = "deviceId") String deviceId,
                         @RequestParam(name = "sensorAddr") String sensorAddr) {
        return sensorService.deletesensor(deviceId, sensorAddr);
    }

    /***************检查连接*****************/

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public String linktest(@RequestBody Sensor sensor) {
        return downDataDao.addTest(sensor, sensor.getDeviceId());
    }
}
